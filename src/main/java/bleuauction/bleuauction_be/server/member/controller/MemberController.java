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
    private final UpdateMemberService updateMemberService;


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
  public ResponseEntity<Map<String, String>> form(
          @CookieValue(required = false) String memberEmail) {
    Map<String, String> response = new HashMap<>();
    response.put("memberEmail", memberEmail);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/delete")
  public void delete(Long memberNo) throws Exception {
    memberRepository.findById(memberNo).ifPresentOrElse(memberRepository::delete,
            () -> new MemberNotFoundException("해당 번호의 회원이 없습니다."));
  }

  @GetMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String  authorizationHeader) throws Exception {
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
    log.error("로그인 정보 확인 >>> {} ,  >>> {}", loginRequest.getMemberEmail(),
            loginRequest.getMemberPwd());

    try {
      if (loginRequest != null) {
        Member loginUser = memberRepository.findByMemberEmail(loginRequest.getMemberEmail())
                .orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 이메일 입니다!"));

        if (!passwordEncoder.matches(loginRequest.getMemberPwd(),
                loginUser.getMemberPwd())) {
          throw new MemberNotFoundException("패스워드가 유효하지 않습니다!");
        }

        Map<String, Object> authMap = new HashMap<>();
        TokenMember tokenMember = new TokenMember(loginUser.getMemberNo(), loginUser.getMemberEmail(), loginUser.getMemberName(), loginUser.getMemberCategory() + "");
        String accessToken = createJwt.createAccessToken(tokenMember);
        String refreshToken = createJwt.createRefreshToken(tokenMember, accessToken);

        authMap.put("accessToken", accessToken);
        authMap.put("refreshToken", refreshToken);
        authMap.put("loginUser", loginUser);
        return ResponseEntity.ok(authMap);
      } else {
        throw new MemberNotFoundException("이메일과 패스워드를 올바르게 입력해주십시오.");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping("/accTokRefresh")
  public ResponseEntity<?> refreshAccessToken(
          @RequestBody RefreshTokenRequest refreshTokenRequest) {
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
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(CreateJwt.REFRESH_ACCESS_TOKEN_ERROR);
    }
  }

  // 회원정보수정
  @PostMapping("/update")
  public ResponseEntity<?> updateMember(
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestPart("updateMemberRequest") UpdateMemberRequest updateMemberRequest,
          @RequestPart("profileImage") MultipartFile profileImage) throws Exception {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(
            authorizationHeader,
            createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }
    Long memberNo = updateMemberRequest.getMemberNo();
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());
    try {
//            String encryptedPassword = passwordEncoder.encode(member.getMemberPwd());
//            member.setMemberPwd(encryptedPassword);

      String newEncryptedPassword = passwordEncoder.encode(updateMemberRequest.getMemberPwd());
      log.info("새 암호가 설정되었습니다." + newEncryptedPassword);

      // 비밀번호를 업데이트할 때 BCrypt로 암호화된 비밀번호 설정
      updateMemberRequest.setMemberPwd(newEncryptedPassword);

      updateMemberService.updateMember(memberNo, updateMemberRequest, profileImage);
      // 첨부 파일 목록 추가
      List<Attach> attaches = new ArrayList<>();
      if (profileImage != null) {
        log.info("첨부 파일 이름: {}", profileImage.getOriginalFilename());
        if (profileImage.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                  "bleuauction-bucket", "member/", profileImage);
          attach.setMemberNo(loginUser.get());
          attaches.add(attach);
        }
      }
      // 첨부 파일 저장 및 결과를 insertAttaches에 할당
      ArrayList<Attach> insertAttaches = (ArrayList<Attach>) attachService.addAttachs(
              (ArrayList<Attach>) attaches);

      updateMemberService.updateMember(memberNo, updateMemberRequest, profileImage);
      log.info("회원정보가 업데이트 되었습니다. 업데이트 된 회원 정보: {}", updateMemberRequest);
      return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
    } catch (MemberNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾을 수 없습니다.");
    }
  }

  // 회원 탈퇴
  @PutMapping("/withdraw")
  public ResponseEntity<?> withdrawMember(@RequestHeader("Authorization") String authorizationHeader) {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
    }
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());
    Long memberNo = tokenMember.getMemberNo();

    if (loginUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }

    try {
      // 회원 상태를 'N'으로 변경하여 탈퇴 처리
      Member member = loginUser.get();
      member.setMemberStatus(MemberStatus.N);
      memberRepository.save(member);

      log.info("회원이 성공적으로 탈퇴되었습니다. 회원번호: {}", member.getMemberNo());

      // 여기에서 토큰을 무효화하는 로직을 추가해야 합니다.
      // 토큰을 무효화하고 클라이언트 측에서도 토큰을 삭제하는 방법을 사용하십시오.

      return ResponseEntity.ok("회원이 성공적으로 탈퇴되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("회원 탈퇴 중 오류가 발생했습니다.");
    }
  }

  // 회원 프로필 삭제
  @DeleteMapping("/delete/profileImage/{fileNo}")
  public ResponseEntity<String> deleteProfileImage(@PathVariable Long fileNo) {
    Attach attach = attachService.getProfileImageByFileNo(fileNo);
    if (attach == null) {
      return new ResponseEntity<>("첨부파일을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }
    boolean isDeleted = attachService.changeProfileImageToDeleteByAttachEntity(attach);
    if (isDeleted) {
      return new ResponseEntity<>("첨부파일이 성공적으로 삭제되었습니다", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("첨부파일 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
