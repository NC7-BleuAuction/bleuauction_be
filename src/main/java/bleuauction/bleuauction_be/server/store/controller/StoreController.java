package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import bleuauction.bleuauction_be.server.store.service.UpdateStoreService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.JwtConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {
  private final CreateJwt createJwt;
  private final MemberRepository memberRepository;
  private final StoreService storeService;
  private final MemberService memberService;
  private final StoreRepository storeRepository;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;


  @GetMapping("/list")
  public ResponseEntity<?> storeList(@RequestHeader("Authorization") String  authorizationHeader, @RequestParam(name = "startPage", defaultValue = "0") int startPage, @RequestParam(name = "pageLowCount", defaultValue = "3") int pageLowCount) throws Exception {
    log.info("url ===========> /store/list");
    log.info("startPage: " + startPage);
    log.info("authorizationHeader: " + authorizationHeader);

    try {
      // 홈에 기본 출력되는 가게리스트에 대한 요청만 예외적으로 토큰검사 제외
      if (authorizationHeader != null && !CreateJwt.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
        if (verificationResult != null) {
          return verificationResult;
        }
      }

      List<Store> storeList = storeService.selectStoreList(StoreStatus.Y, startPage, pageLowCount);
      log.info("storeList: " + storeList);
      return ResponseEntity.ok(storeList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CreateJwt.SERVER_ERROR);
    }
  }

  @GetMapping("{storeNo}")
  public ResponseEntity<Object> detail(@PathVariable Long storeNo) throws Exception {
    Optional<Store> storeOptional = storeRepository.findById(storeNo);

    if (storeOptional.isPresent()) {
      Store store = storeOptional.get();
      return ResponseEntity.ok().body(store);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // 회원 번호로 가게 찾기
  @GetMapping("/detailByMember")
  public ResponseEntity<?> detailByMemberNo(HttpSession session, @RequestParam Member member)
          throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인한 회원 정보를 찾을 수 없습니다.");
    }
    Optional<Store> storeOptional = storeRepository.findByMemberNo(member);

    if (storeOptional.isPresent()) {
      Store store = storeOptional.get();
      return ResponseEntity.ok().body(store);
    } else {
      return ResponseEntity.notFound().build();
    }
  }


  // 가게 등록
  @PostMapping("/signup")
  public Store storeSignUp(HttpSession session, @RequestBody @Valid StoreSignUpRequest request)
          throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인한 회원 정보를 찾을 수 없습니다.");
    }
    Long memberNo = loginUser.getMemberNo(); // 지금 로그인 되어있는 멤버의 아이디를 가져온다
    return storeRepository.save(storeService.signup(request, memberNo));
  }

  // 가게정보수정
  @PutMapping("/update")
  public ResponseEntity<String> updateStore(HttpSession session,
                                            @RequestPart("updateStoreRequest") UpdateStoreRequest updateStoreRequest,
                                            @RequestPart("profileImage") MultipartFile profileImage)
          throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    try {
      UpdateStoreService updateStoreService = new UpdateStoreService(storeRepository);
      // 첨부 파일 목록 추가
      List<Attach> attaches = new ArrayList<>();
      if (profileImage != null) {
        log.info("첨부 파일 이름: {}", profileImage.getOriginalFilename());
        if (profileImage.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                  "bleuauction-bucket", "store/", profileImage);
          attach.setMemberNo(loginUser);
          attaches.add(attach);
        }
      }
      // 첨부 파일 저장 및 결과를 insertAttaches에 할당
      ArrayList<Attach> insertAttaches = (ArrayList<Attach>) attachService.addAttachs(
              (ArrayList<Attach>) attaches);
      // 가게 정보 업데이트
      updateStoreService.updateStore(loginUser.getMemberNo(), updateStoreRequest);
      log.info("가게 정보가 업데이트되었습니다. 업데이트된 가게 정보: {}", updateStoreRequest);
      return ResponseEntity.ok("가게 정보가 업데이트되었습니다.");
    } catch (StoreNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
    }
  }

  // 가게 탈퇴
  @PutMapping("/withdraw/{storeNo}")
  public ResponseEntity<String> withdrawStore(HttpSession session, @PathVariable("storeNo") Long storeNo) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    Optional<Store> storeOptional = storeService.selectStore(storeNo);

    // 체크: storeOptional이 비어있는지 확인
    if (!storeOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
    }
    Store store = storeOptional.get();  // Store 객체 가져오기

    if (store.getMemberNo() == loginUser) {

      // 가게 상태를 'N'으로 변경하여 탈퇴 처리
      store.setStoreStatus(StoreStatus.N);
      storeRepository.save(store);
      // 세션을 무효화하여 로그아웃 처리
      session.invalidate();

      log.info("가게가 성공적으로 폐업되었습니다. 가게번호: {}", loginUser.getMemberNo());
      return ResponseEntity.ok("가게가 성공적으로 폐업되었습니다.");
    } else {
      log.error("가게정보는: {}", ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR));
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("올바른 가게 정보가 아닙니다.");
    }
  }

  // 가게 프로필 삭제
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
