package bleuauction.bleuauction_be.server.store.controller;


import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreRequestUnAuthorizationException;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import bleuauction.bleuauction_be.server.store.service.StoreComponentService;
import bleuauction.bleuauction_be.server.store.service.StoreModuleService;
import java.util.List;
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
    private final JwtUtils jwtUtils;
    private final MemberModuleService memberModuleService;
    private final AttachComponentService attachComponentService;
    private final StoreComponentService storeComponentService;
    private final StoreModuleService storeModuleService;

    /**
     * 가게정보 리스트를 반환하는 기능
     *
     * @param authorizationHeader
     * @param page
     * @param limit
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Store>> storeList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageLowCount", defaultValue = "3") int limit) {
        log.info(
                "[StoreController] GetStoreList, StartPage >>> {}, pageLowCount >>> {}, Authorization >>> {}",
                page,
                limit,
                authorizationHeader);

        // 홈에 기본 출력되는 가게리스트에 대한 요청만 예외적으로 토큰검사 제외
        if (!authorizationHeader.isEmpty()
                && !jwtUtils.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
            jwtUtils.verifyToken(authorizationHeader);
        }
        return ResponseEntity.ok(storeComponentService.selectStoreList(StoreStatus.Y, page, limit));
    }

    /**
     * 가게의 Detail한 정보를 받아갈때 사용 [TODO] : Token 확인이 필요 없는 곳인가..?
     *
     * @param storeNo
     * @return
     */
    @GetMapping("/{storeNo}")
    public ResponseEntity<Store> detail(
            @PathVariable Long storeNo,
            @RequestHeader("Authorization") String authorizationHeader) {
        // 홈에 기본 출력되는 가게리스트에 대한 요청만 예외적으로 토큰검사 제외
        if (!authorizationHeader.isEmpty()
                && !jwtUtils.UNAUTHORIZED_ACCESS.equals(authorizationHeader)) {
            jwtUtils.verifyToken(authorizationHeader);
        }
        return ResponseEntity.ok().body(storeModuleService.findById(storeNo));
    }

    /**
     * 요청한 사용자와 정보조회를 희망하는 사용자가 일치하는 경우 해당 사용자의 가게정보를 반환한다.
     *
     * @param authorizationHeader
     * @param memberNo
     * @return
     */
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<Store> detailByMemberNo(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long memberNo) {
        // S : 인증 인가, 검증로직
        jwtUtils.verifyToken(authorizationHeader);
        Member requestUser =
                memberModuleService.findById(
                        jwtUtils.getTokenMember(authorizationHeader).getMemberNo());
        Member targetUser = memberModuleService.findById(memberNo);
        if (!requestUser.getId().equals(targetUser.getId())) {
            throw new StoreUpdateUnAuthorizedException(requestUser, targetUser);
        }
        // E : 인증 인가, 검증로직

        return ResponseEntity.ok(storeModuleService.findByMember(targetUser));
    }

    // 가게등록
    @PostMapping
    public ResponseEntity<String> storeSignUp(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody StoreSignUpRequest request)
            throws Exception {
        jwtUtils.verifyToken(authorizationHeader);

        Member loginUser =
                memberModuleService.findById(
                        jwtUtils.getTokenMember(authorizationHeader).getMemberNo());
        verifyIsMemberCategorySeller(loginUser);

        // StoreService를 사용하여 가게 등록 및 중복 검사
        storeComponentService.signup(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Store created successfully");
    }

    // 가게정보수정
    @PutMapping
    public ResponseEntity<String> updateStore(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("updateStoreRequest") UpdateStoreRequest updateStoreRequest)
            throws Exception {
        jwtUtils.verifyToken(authorizationHeader);

        Member loginUser =
                memberModuleService.findById(
                        jwtUtils.getTokenMember(authorizationHeader).getMemberNo());
        verifyIsMemberCategorySeller(loginUser);

        storeComponentService.updateStore(loginUser, updateStoreRequest);
        return ResponseEntity.ok("가게 정보가 업데이트되었습니다.");
    }

    /**
     * 가게정보를 삭제한다.
     *
     * @param authorizationHeader
     * @param storeNo
     * @return
     */
    @DeleteMapping("/{storeNo}")
    public ResponseEntity<String> withdrawStore(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("storeNo") Long storeNo) {
        jwtUtils.getTokenMember(authorizationHeader);

        // 가게 정보 확인
        storeComponentService.withDrawStore(
                storeNo,
                memberModuleService.findById(
                        jwtUtils.getTokenMember(authorizationHeader).getMemberNo()));

        // TODO: 토큰 무효화 (예: Token을 Blacklist에 추가하고, 클라이언트 측에서 로컬 스토리지 또는 쿠키에서 토큰 제거)
        return ResponseEntity.ok("가게가 성공적으로 폐업되었습니다.");
    }

    /**
     * 가게의 프로필 이미지를 삭제하는 기능으로 <br>
     * 해당 기능은 Controller가 적합함. <br>
     * [TODO] : 현재 해당 기능의 문제점은 인증 인가없이 그냥 fileNo를 입력할때 삭제가 된다는 점, 그러므로 타인이 삭제시킬수도 있음. 추후 보완이 필요하다.
     *
     * @param fileNo
     * @return
     */
    @DeleteMapping("/profile/{fileNo}")
    public ResponseEntity<String> deleteProfileImage(@PathVariable("fileNo") Long fileNo) {
        return (FileStatus.N.equals(
                        attachComponentService
                                .changeFileStatusDeleteByFileNo(fileNo)
                                .getFileStatus()))
                ? ResponseEntity.ok("Profile Image Delete Success")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Profile Image Delete Failed");
    }

    private void verifyIsMemberCategorySeller(Member member) {
        if (!MemberCategory.isMemberSeller(member.getCategory())) {
            throw new StoreRequestUnAuthorizationException(member);
        }
    }
}
