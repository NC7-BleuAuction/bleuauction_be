package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.util.TokenMember;
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
import bleuauction.bleuauction_be.server.util.CreateJwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;
  private final MenuRepository menuRepository;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final EntityManager entityManager;
  private final CreateJwt createJwt;
  private final MemberService memberService;
  private final StoreRepository storeRepository;

  //등록
  @GetMapping("/api/menu/new")
  public MenuForm createForm() {
    MenuForm menuForm = new MenuForm();
    return menuForm;
  }

  //등록처리
  @PostMapping("/api/menu/new")
  @Transactional
  public ResponseEntity<?> menu(@RequestHeader("Authorization") String  authorizationHeader,  HttpSession session, Menu menu, @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Long memberId = loginUser.get().getMemberNo();

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = entityManager.createQuery("SELECT s FROM Store s WHERE s.memberNo.memberNo = :memberId", Store.class)
            .setParameter("memberId", memberId)
            .getSingleResult();

    if (store == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not associated with a store.");
    }

    menu.setStoreNo(store);

    if (multipartFiles != null && multipartFiles.size() > 0) {
      ArrayList<Attach> attaches = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFiles) {
        if (multipartFile.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                  "bleuauction-bucket", "menu/", multipartFile);
          menu.addAttach(attach);
        }
      }
    }

    menu = entityManager.merge(menu);
    menuService.enroll(menu);
    log.info("menu/postnew");
    return ResponseEntity.status(HttpStatus.CREATED).body("Menu created successfully");
  }

  //가게별 목록 조회
  @GetMapping(value = "/api/menu/{storeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Menu> findMenusByStoreNo(@PathVariable("storeNo") Long storeNo) throws Exception {
    try {
      List<Menu> menus = menuRepository.findMenusByStoreNo(storeNo);
      return menus;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
  //가게(회원)별 목록 조회
  @GetMapping(value = "/api/menu/store", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findMenusByStoreNo(@RequestHeader("Authorization") String  authorizationHeader) throws Exception {
    try {

      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      // 로그인 유저의 멤버 번호
      Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

      Optional<Store> store = storeRepository.findByMemberNo(loginUser.get());

      List<Menu> menus = menuRepository.findMenusByStoreNo(store.get().getStoreNo());
      return ResponseEntity.ok(menus);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("등록된 메뉴가 없습니다.");
    }
  }

  //목록 조회
  @GetMapping(value = "/api/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Menu> findmenus() throws Exception {
    try {
      List<Menu> menus = menuService.findmenus();
      return menus;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  //삭제
  @PostMapping("/api/menu/delete/{menuNo}")
  public ResponseEntity<?> deleteMenu(TokenMember tokenMember, @RequestHeader("Authorization") String  authorizationHeader, HttpSession session, @PathVariable("menuNo") Long menuNo) {
    Menu menu = menuService.findOne(menuNo);

    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Long memberId = loginUser.get().getMemberNo();

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = entityManager.createQuery("SELECT s FROM Store s WHERE s.memberNo.memberNo = :memberId", Store.class)
            .setParameter("memberId", memberId)
            .getSingleResult();

    if (menu.getStoreNo() == store) {

      // 사진 상태를 'N'으로 변경
      for (Attach attach : menu.getMenuAttaches()) {
        attachService.update(attach.getFileNo());
      }
      menuService.deleteMenu(menuNo);
      return ResponseEntity.ok("Menu deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다.");
    }
  }


  //사진삭제
  @DeleteMapping("/api/menu/deletefile/{fileNo}")
  public ResponseEntity<String> fileMenuDelete(@PathVariable Long fileNo) {
    attachService.update(fileNo);
    return ResponseEntity.ok("File deleted successfully");
  }


  //디테일(수정)
  @GetMapping("/api/menu/detail/{menuNo}")
  public ResponseEntity<Menu> detailMenu(HttpSession session, @PathVariable("menuNo") Long menuNo) {
    Menu menu = menuService.findOne(menuNo);
    // 예를 들어, Menu 객체에 Attach 정보가 있을 경우:
    //Attach attach = menu.getMenuAttaches().isEmpty() ? null : menu.getMenuAttaches().get(0);

    // Menu 엔티티와 Attach 엔티티를 함께 반환
    menu.setMenuAttaches(menu.getMenuAttaches());
    return ResponseEntity.ok(menu);
  }

  @PostMapping("/api/menu/update/{menuNo}")
  public ResponseEntity<?> updateMenu(TokenMember tokenMember, @RequestHeader("Authorization") String  authorizationHeader, HttpSession session, Menu menu,
                                           @PathVariable("menuNo") Long menuNo,
                                           @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {

    Menu updatedMenu = menuService.findOne(menuNo);


    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Long memberId = loginUser.get().getMemberNo();
    log.info("memberId ? " + memberId);

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = entityManager.createQuery("SELECT s FROM Store s WHERE s.memberNo.memberNo = :memberId", Store.class)
            .setParameter("memberId", memberId)
            .getSingleResult();

    log.info("store ? " + store);

    if (updatedMenu.getStoreNo() == store) {

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "menu/", multipartFile);
            updatedMenu.addAttach(attach);
          }
        }
      }

      menuService.update(menu);
      log.info("menu/update");
      return ResponseEntity.ok("Menu updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 권한이 없습니다.");
    }
  }
}
