package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {
    private final CreateJwt createJwt;
    private final StoreService storeService;
    private final MemberService memberService;
    private final NcpObjectStorageService ncpObjectStorageService;
    private final AttachService attachService;

    /**
     * 가게정보 리스트를 반환하는 기능
     * [TODO] : 추후 Return의 Generic을 ?가 아닌 List로 변경해야할듯 <br/>
     *
     * @param authorizationHeader
     * @param page
     * @param limit
     * @return
     */
    @GetMapping
    public ResponseEntity<?> storeList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageLowCount", defaultValue = "3") int limit
    ) {
        log.info("[StoreController] GetStoreList, StartPage >>> {}, pageLowCount >>> {}, Authorization >>> {}"
                , page, limit, authorizationHeader);

        // [TODO] : JWT기능 void로 변경되면 로직 바꾸기
        if (authorizationHeader != null && !CreateJwt.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
            ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
            if (verificationResult != null) {
                return verificationResult;
            }
        }
        return ResponseEntity.ok(storeService.selectStoreList(StoreStatus.Y, page, limit));
    }

    /**
     * 가게의 Detail한 정보를 받아갈때 사용
     * [TODO] : Token 확인이 필요 없는 곳인가..?
     *
     * @param storeNo
     * @return
     */
    @GetMapping("/{storeNo}")
    public ResponseEntity<?> detail(
            @PathVariable Long storeNo,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        // [TODO] : JWT기능 void로 변경되면 로직 바꾸기
        if (authorizationHeader != null && !CreateJwt.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
            ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
            if (verificationResult != null) {
                return verificationResult;
            }
        }

        return ResponseEntity.ok().body(storeService.findStoreById(storeNo));
    }

    /**
     * 요청한 사용자와 정보조회를 희망하는 사용자가 일치하는 경우 해당 사용자의 가게정보를 반환한다.
     *
     * @param authorizationHeader
     * @param memberNo
     * @return
     */
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<?> detailByMemberNo(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long memberNo
    ) {
        // [TODO] : JWT기능 void로 변경되면 로직 바꾸기
        // S : 인증 인가, 검증로직
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
        if (verificationResult != null) {
            return verificationResult;
        }
        Member requestUser = memberService.findMemberById(createJwt.getTokenMember(authorizationHeader).getMemberNo());
        Member targetUser = memberService.findMemberById(memberNo);
        if (!requestUser.getMemberNo().equals(targetUser.getMemberNo())) {
            throw new StoreUpdateUnAuthorizedException(requestUser, targetUser);
        }
        // E : 인증 인가, 검증로직

        return ResponseEntity.ok(storeService.findStoreByMember(targetUser));
    }

    // 가게등록
    @PostMapping
    public ResponseEntity<?> storeSignUp(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody StoreSignUpRequest request
    ) throws Exception {
        // [TODO] : JWT기능 void로 변경되면 로직 바꾸기
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
        if (verificationResult != null) {
            return verificationResult;
        }

        Member loginUser = memberService.findMemberById(createJwt.getTokenMember(authorizationHeader).getMemberNo());
        if (!MemberCategory.S.equals(loginUser.getMemberCategory())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
        }

        try {
            // StoreService를 사용하여 가게 등록 및 중복 검사
            storeService.signup(request, loginUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("Store created successfully");
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 가게입니다.");
        }
    }

    // 가게정보수정
    @PutMapping
    public ResponseEntity<?> updateStore(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("updateStoreRequest") UpdateStoreRequest updateStoreRequest
    ) throws Exception {
        // [TODO] : JWT기능 void로 변경되면 로직 바꾸기
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
        if (verificationResult != null) {
            return verificationResult;
        }

        Member loginUser = memberService.findMemberById(createJwt.getTokenMember(authorizationHeader).getMemberNo());
        if (!MemberCategory.S.equals(loginUser.getMemberCategory())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("판매자 권한이 필요합니다");
        }

        try {
            // JWT토큰을 기반으로 한 로그인 사용자의 마켓정보 획득
            Store store = storeService.findStoreByMember(loginUser);

            // TODO : 추후 Service레벨에 Attach Service넣을 수 있도록 구조 개선이 필요함;
            // 첨부 파일 목록 추가
            if (updateStoreRequest.getProfileImage() != null && updateStoreRequest.getProfileImage().getSize() > 0) {
                log.info("첨부 파일 이름: {}", updateStoreRequest.getProfileImage().getOriginalFilename());

                Attach attach = ncpObjectStorageService.uploadFile("bleuauction-bucket", "store/", updateStoreRequest.getProfileImage());
                attach.setStoreNo(store);

                // 첨부 파일 저장 및 결과를 insertAttaches에 할당 및 Attach정보에 대해서는 Store객체에 추가
                attachService.insertAttach(attach);
                store.addAttaches(attach);
            }
            // 가게 정보 업데이트
            storeService.updateStore(store, updateStoreRequest);
            log.info("가게 정보가 업데이트되었습니다. 업데이트된 가게 정보: {}", updateStoreRequest);
            return ResponseEntity.ok("가게 정보가 업데이트되었습니다.");
        } catch (StoreNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
        }
    }

    // 가게 삭제
    @DeleteMapping("/{storeNo}")
    public ResponseEntity<?> withdrawStore(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("storeNo") Long storeNo) {
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
        if (verificationResult != null) {
            return verificationResult;
        }
        // 요청자 정보
        Member requestMember = memberService.findMemberById(createJwt.getTokenMember(authorizationHeader).getMemberNo());

        // 가게 정보 확인
        storeService.withDrawStore(storeService.findStoreById(storeNo), requestMember);

        // TODO: 토큰 무효화 (예: Token을 Blacklist에 추가하고, 클라이언트 측에서 로컬 스토리지 또는 쿠키에서 토큰 제거)
        return ResponseEntity.ok("가게가 성공적으로 폐업되었습니다.");
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
