package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {

  private final MenuService menuService;
  private final StoreService storeService;
  private final AttachService attachService;
  private final CreateJwt createJwt;
  private final MemberService memberService;
  private final StoreRepository storeRepository;


  //등록
  @PostMapping("/new")
  @Transactional
  public ResponseEntity<?> menu(@RequestHeader("Authorization") String  authorizationHeader,
                                @RequestBody Menu menu,
                                @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = storeService.findStoreByMember(loginUser.get());

    if (store == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("메뉴 등록 권한이 없습니다.");
    }

    menuService.enroll(menu, store, multipartFiles);
    log.info("menu/postnew");
    return ResponseEntity.status(HttpStatus.CREATED).body("Menu created successfully");
  }

  //가게별 목록 조회
  @GetMapping(value = "/{storeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Menu> findMenusByStoreNo(@PathVariable("storeNo") Long storeNo) throws Exception {
    return menuService.findMenusByStoreNo(storeNo);
  }

  //가게(회원)별 목록 조회
  @GetMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<?> findMenusByStoreNo(@RequestHeader("Authorization") String authorizationHeader)  {
      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      // 로그인 유저의 멤버 번호
      Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());
      Optional<Store> store = storeRepository.findByMemberNo(loginUser.get());

      return menuService.findMenusByStoreNoAndStatus(store.get().getStoreNo(), MenuStatus.Y);
  }

  //삭제
  @DeleteMapping
  public ResponseEntity<?> deleteMenu(@RequestHeader("Authorization") String  authorizationHeader,
                                      @PathVariable("menuNo") Long menuNo) {

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = storeService.findStoreByMember(loginUser.get());
    menuService.deleteMenuByMenuNoAndStore(menuNo, store);
    return ResponseEntity.ok("Menu deleted successfully");
  }


  //사진삭제
  @DeleteMapping("/{fileNo}")
  public ResponseEntity<String> fileMenuDelete(@PathVariable Long fileNo) {
    attachService.changeFileStatusToDeleteByFileNo(fileNo);
    return ResponseEntity.ok("File deleted successfully");
  }


  //수정
  @PutMapping("/{menuNo}")
  public ResponseEntity<?> updateMenu(@RequestHeader("Authorization") String  authorizationHeader,
                                      @PathVariable("menuNo") Long menuNo,
                                      @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {

    Menu updatedMenu = menuService.findOne(menuNo);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Store store = storeService.findStoreByMember(loginUser.get());

    menuService.update(updatedMenu, multipartFiles, store);
    return ResponseEntity.ok("Menu updated successfully");
  }
}
