package bleuauction.bleuauction_be.server.menu.controller;


import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.menu.dto.MenuDTO;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.service.MenuComponentService;
import bleuauction.bleuauction_be.server.menu.service.MenuModuleService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreModuleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuComponentService menuComponentService;
    private final MenuModuleService menuModuleService;
    private final StoreModuleService storeModuleService;
    private final AttachComponentService attachComponentService;
    private final JwtUtils jwtUtils;
    private final MemberModuleService memberModuleService;

    // 등록
    @PostMapping("/new")
    @Transactional
    public ResponseEntity<String> menu(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Menu menu,
            @RequestParam(name = "multipartFiles", required = false)
                    List<MultipartFile> multipartFiles) {
        jwtUtils.verifyToken(authorizationHeader);
        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

        // Member ID를 사용하여 관련된 Store를 찾습니다.
        Store store = storeModuleService.findByMember(loginUser);

        if (store == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("메뉴 등록 권한이 없습니다.");
        }
        menuComponentService.enroll(menu, store, multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body("Menu created successfully");
    }

    // 가게별 목록 조회
    @GetMapping(value = "/{storeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> findMenusByStoreNo(@PathVariable("storeNo") Long storeNo) throws Exception {
        return menuModuleService.findAllByStore(storeModuleService.findById(storeNo));
    }

    // 가게(회원)별 목록 조회
    @GetMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> findMenusByStoreNo(
            @RequestHeader("Authorization") String authorizationHeader) {
        jwtUtils.verifyToken(authorizationHeader);

        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        // 로그인 유저의 멤버 번호
        Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());
        Store store = storeModuleService.findByMember(loginUser);
        return menuModuleService.findAllByStore(store);
    }

    @DeleteMapping("/{menuNo}")
    public ResponseEntity<String> deleteMenu(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("menuNo") Long menuNo) {

        jwtUtils.verifyToken(authorizationHeader);
        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

        // Member ID를 사용하여 관련된 Store를 찾습니다.
        Store store = storeModuleService.findByMember(loginUser);
        menuComponentService.deleteMenuByMenuNoAndStore(menuNo, store);
        return ResponseEntity.ok("Menu deleted successfully");
    }

    // 사진삭제
    @DeleteMapping("/file/{fileNo}")
    public ResponseEntity<String> fileMenuDelete(@PathVariable Long fileNo) {
        attachComponentService.changeFileStatusDeleteByFileNo(fileNo);
        return ResponseEntity.ok("File deleted successfully");
    }

    // 수정
    @PutMapping("/{menuNo}")
    public ResponseEntity<String> updateMenu(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("menuNo") Long menuNo,
            @RequestBody MenuDTO request,
            @RequestParam(name = "multipartFiles", required = false)
                    List<MultipartFile> multipartFiles) {
        jwtUtils.verifyToken(authorizationHeader);
        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

        Store store = storeModuleService.findByMember(loginUser);

        menuComponentService.update(menuNo, multipartFiles, store, request);
        return ResponseEntity.ok("Menu updated successfully");
    }
}
