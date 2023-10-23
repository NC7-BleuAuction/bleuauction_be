package bleuauction.bleuauction_be.server.member.controller;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.dto.LoginRequest;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.member.service.UpdateMemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;

import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.RefreshTokenRequest;
import bleuauction.bleuauction_be.server.util.TokenMember;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.POST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
  private final CreateJwt createJwt;
  private final MemberRepository memberRepository;
  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final Member member;


  @GetMapping("/{memberNo}")
  public ResponseEntity<Object> detail(@PathVariable Long memberNo) throws Exception {
    Optional<Member> memberOptional = memberRepository.findById(memberNo);

    if (memberOptional.isPresent()) {

      Member member = memberOptional.get();
      return ResponseEntity.ok().body(member);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Member>> list() throws Exception {
    List<Member> members = memberRepository.findAll();
    return ResponseEntity.ok().body(members);
  }

  @GetMapping("/form")
  public ResponseEntity<Map<String, String>> form(@CookieValue(required = false) String memberEmail) {
    Map<String, String> response = new HashMap<>();
    response.put("memberEmail", memberEmail);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/delete")
  public void delete(Long memberNo) throws Exception {
    memberRepository.findById(memberNo).ifPresentOrElse(memberRepository::delete, () -> new MemberNotFoundException("해당 번호의 회원이 없습니다."));
  }

  @GetMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) throws Exception {
    session.invalidate();
    log.info("Call logout");
    return ResponseEntity.ok().body("{\"message\": \"Logout successful\"}");
  }

  // 일반사용자 회원가입
  @PostMapping("/signup")
  public Member signUp(Member member) throws Exception {
    log.info("member: " + member);
    log.error("Email:[{}], Password:[{}]", member.getMemberEmail(), member.getMemberPwd());
    // 비밀번호를 암호화하여 저장
    String encryptedPassword = passwordEncoder.encode(member.getMemberPwd());
    member.setMemberPwd(encryptedPassword);

    // 회원 저장
    return memberRepository.save(member);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
    log.error("로그인 정보 확인 >>> {} ,  >>> {}", loginRequest.getMemberEmail(), loginRequest.getMemberPwd());

    try {
      if (loginRequest != null) {
        Member loginUser = memberRepository.findByMemberEmail(loginRequest.getMemberEmail()).orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 이메일 입니다!"));

        if (!passwordEncoder.matches(loginRequest.getMemberPwd(), loginUser.getMemberPwd())) {
          throw new MemberNotFoundException("패스워드가 유효하지 않습니다!");
        }

        TokenMember tokenMember = new TokenMember(loginUser.getMemberNo(), loginUser.getMemberEmail(), loginUser.getMemberName(), loginUser.getMemberCategory().toString());
        Map<String, Object> tokenMap = new HashMap<>();
        String accessToken = createJwt.createAccessToken(tokenMember);
        String refreshToken = createJwt.createRefreshToken(tokenMember, accessToken);

        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        return ResponseEntity.ok(tokenMap);
      } else {
        throw new MemberNotFoundException("이메일과 패스워드를 올바르게 입력해주십시오.");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping("/accTokRefresh")
  public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    log.info("url ===========> /member/accTokRefresh");

    try {
      String refreshToken = refreshTokenRequest.getRefreshToken();
      log.info("refreshToken: " + refreshToken);

      Map<String, Object> tokenMap = new HashMap<>();
      String renewAccessToken = createJwt.getRenewAccessToken(refreshToken);

      if (refreshToken == null) {
        new Exception();
      }

      tokenMap.put("accessToken", renewAccessToken);
      return ResponseEntity.ok(tokenMap);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CreateJwt.REFRESH_ACCESS_TOKEN_ERROR);
    }
  }

  // 회원정보수정
  @PostMapping("/update")
  public ResponseEntity<String> updateMember(HttpSession session, UpdateMemberRequest updateMemberRequest, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    try {

      // UpdateMemberService 클래스를 사용하여 회원 정보 업데이트
      UpdateMemberService updateMemberService = new UpdateMemberService(memberRepository);
      // 첨부 파일 목록 추가
      List<Attach> attaches = new ArrayList<>();
      if (profileImage != null) {
        log.info("첨부 파일 이름: {}", profileImage.getOriginalFilename());
        if (profileImage.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(), "bleuauction-bucket", "member/", profileImage);
          attach.setMemberNo(loginUser);
          attaches.add(attach);
        }
      }
      // 첨부 파일 저장 및 결과를 insertAttaches에 할당
      ArrayList<Attach> insertAttaches = (ArrayList<Attach>) attachService.addAttachs((ArrayList<Attach>) attaches);
      // 회원 정보 업데이트
      updateMemberService.updateMember(loginUser.getMemberNo(), updateMemberRequest);
      log.info("회원 정보가 업데이트되었습니다. 업데이트된 회원 정보: {}", updateMemberRequest);
      return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
    } catch (MemberNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾을 수 없습니다.");
    }
  }

  // 회원 탈퇴
  @PutMapping("/withdraw")
  public ResponseEntity<String> withdrawMember(HttpSession session) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    try {
      // 회원 상태를 'N'으로 변경하여 탈퇴 처리
      loginUser.setMemberStatus(MemberStatus.N);
      memberRepository.save(loginUser);

      // 세션을 무효화하여 로그아웃 처리
      session.invalidate();

      log.info("회원이 성공적으로 탈퇴되었습니다. 회원번호: {}", loginUser.getMemberNo());
      return ResponseEntity.ok("회원이 성공적으로 탈퇴되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 중 오류가 발생했습니다.");
    }
  }

  // 회원 프로필 삭제
  @DeleteMapping("/delete/profileImage/{fileNo}")
  public ResponseEntity<String> deleteProfileImage(@PathVariable Long fileNo) {
    Attach attach = attachService.getProfileImageByFileNo(fileNo);
    if (attach == null) {
      return new ResponseEntity<>("첨부파일을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }
    boolean isDeleted = attachService.deleteProfileImage(attach);
    if (isDeleted) {
      return new ResponseEntity<>("첨부파일이 성공적으로 삭제되었습니다", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("첨부파일 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
