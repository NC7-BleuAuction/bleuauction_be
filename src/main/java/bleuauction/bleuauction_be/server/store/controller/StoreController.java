package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
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
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
  private final UpdateStoreService updateStoreService;
  private final EntityManager entityManager;


  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Store>> storeList(@RequestHeader("Authorization") String  authorizationHeader,
          @RequestParam(name = "startPage", defaultValue = "0")
          int startPage,
          @RequestParam(name = "pageLowCount", defaultValue = "3")
          int pageLowCount) throws Exception {
    log.info("url ===========> /store/list");
    log.info("startPage: " + startPage);
    log.info("authorizationHeader: " + authorizationHeader);

      // 홈에 기본 출력되는 가게리스트에 대한 요청만 예외적으로 토큰검사 제외
      if (!authorizationHeader.isEmpty() && !CreateJwt.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
        createJwt.verifyAccessToken(authorizationHeader);
      }

      List<Store> storeList = storeService.selectStoreList(StoreStatus.Y, startPage, pageLowCount);
      log.info("storeList: " + storeList);
      return ResponseEntity.ok(storeList);
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
  public ResponseEntity<?> detailByMemberNo(@RequestHeader("Authorization") String authorizationHeader,
          @RequestParam Member member) {
    createJwt.verifyAccessToken(
            authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    Optional<Store> storeOptional = storeRepository.findByMemberNo(member);

    if (storeOptional.isPresent()) {
      Store store = storeOptional.get();
      return ResponseEntity.ok().body(store);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // 가게등록
  @PostMapping("/signup")
  public ResponseEntity<?> storeSignUp(@RequestHeader("Authorization") String authorizationHeader,
          @RequestBody StoreSignUpRequest request) throws Exception {

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    if (loginUser.isPresent() && loginUser.get().getMemberCategory() == MemberCategory.S) {
      try {
        // StoreService를 사용하여 가게 등록 및 중복 검사
        Store store = storeService.signup(request, tokenMember.getMemberNo());

        return ResponseEntity.status(HttpStatus.CREATED).body("Store created successfully");
      } catch (IllegalAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
      } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 가게입니다.");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
    }
  }

  // 가게정보수정
  @PutMapping("/update")
  public ResponseEntity<?> updateStore(@RequestHeader("Authorization") String authorizationHeader,
          @RequestPart("updateStoreRequest") UpdateStoreRequest updateStoreRequest,
          @RequestPart("profileImage") MultipartFile profileImage) {

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Long memberId = loginUser.get().getMemberNo();
    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = entityManager.createQuery("SELECT s FROM Store s WHERE s.memberNo.memberNo = :memberId", Store.class)
            .setParameter("memberId", memberId)
            .getSingleResult();


    if (loginUser.isPresent() && loginUser.get().getMemberCategory() == MemberCategory.S) {
      try {

        // 첨부 파일 목록 추가
        List<Attach> attaches = new ArrayList<>();
        if (profileImage != null) {
          log.info("첨부 파일 이름: {}", profileImage.getOriginalFilename());

          if (profileImage.getSize() > 0) {

            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "store/", profileImage);
            attach.setStoreNo(store);
            attaches.add(attach);
          }
        }
        // 첨부 파일 저장 및 결과를 insertAttaches에 할당
        attachService.addAttachs(attaches);
        // 가게 정보 업데이트
        updateStoreService.updateStore(store.getStoreNo(), updateStoreRequest, profileImage);
        log.info("가게 정보가 업데이트되었습니다. 업데이트된 가게 정보: {}", updateStoreRequest);
        return ResponseEntity.ok("가게 정보가 업데이트되었습니다.");
      } catch (StoreNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
    }
  }

  // 가게 삭제
  @PutMapping("/withdraw/{storeNo}")
  public ResponseEntity<?> withdrawStore(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("storeNo") Long storeNo) {
    createJwt.verifyAccessToken(authorizationHeader);

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

    // 가게 정보 확인
    Optional<Store> storeOptional = storeService.selectStore(storeNo);

    if (storeOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
    }

    Store store = storeOptional.get();

    // 가게 소유자 확인
    if (store.getMemberNo().getMemberNo().equals(tokenMember.getMemberNo())) {
      // 가게 상태를 'N'으로 변경하여 탈퇴 처리
      store.setStoreStatus(StoreStatus.N);
      storeRepository.save(store);

      // TODO: 토큰 무효화 (예: Token을 Blacklist에 추가하고, 클라이언트 측에서 로컬 스토리지 또는 쿠키에서 토큰 제거)

      log.info("가게가 성공적으로 폐업되었습니다. 가게번호: {}", tokenMember.getMemberNo());
      return ResponseEntity.ok("가게가 성공적으로 폐업되었습니다.");
    } else {
      log.error("올바른 가게 정보가 아닙니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("올바른 가게 정보가 아닙니다.");
    }
  }

  /**
   * 가게의 프로필 이미지를 삭제하는 기능으로 <br />
   * 해당 기능은 Controller가 적합함. <br />
   * [TODO] : 현재 해당 기능의 문제점은 인증 인가없이 그냥 fileNo를 입력할때 삭제가 된다는 점, 그러므로 타인이 삭제시킬수도 있음. 추후 보완이 필요하다.
   *
   * @param fileNo
   * @return
   */
  @DeleteMapping("/delete/profileImage/{fileNo}")
  public ResponseEntity<String> deleteProfileImage(@PathVariable("fileNo") Long fileNo) {
    if (FileStatus.N.equals(attachService.changeFileStatusToDeleteByFileNo(fileNo).getFileStatus())) {
      return ResponseEntity.ok("Profile Image Delete Success");
    }
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Profile Image Delete Failed");
  }
}
