package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;
  private final MenuRepository menuRepository;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final EntityManager entityManager;

  //등록
  @GetMapping("/api/menu/new")
  public MenuForm createForm() {
    MenuForm menuForm = new MenuForm();
    return menuForm;
  }

  //등록처리
  @PostMapping("/api/menu/new")
  @Transactional
  public ResponseEntity<String> menu(HttpSession session, Menu menu, @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    Long memberId = loginUser.getMemberNo();

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
  public ResponseEntity<String> deleteMenu(HttpSession session, @PathVariable("menuNo") Long menuNo) {
    Menu menu = menuService.findOne(menuNo);

    Member loginUser = (Member) session.getAttribute("loginUser");
    Long memberId = loginUser.getMemberNo();

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
  @ResponseBody
  public ResponseEntity<Menu> detailMenu(HttpSession session, @PathVariable("menuNo") Long menuNo) {
    Menu menu = menuService.findOne(menuNo);
    // 예를 들어, Menu 객체에 Attach 정보가 있을 경우:
    //Attach attach = menu.getMenuAttaches().isEmpty() ? null : menu.getMenuAttaches().get(0);

    // Menu 엔티티와 Attach 엔티티를 함께 반환
    menu.setMenuAttaches(menu.getMenuAttaches());
    return ResponseEntity.ok(menu);
  }

  @PostMapping("/api/menu/update/{menuNo}")
  public ResponseEntity<String> updateMenu(HttpSession session, Menu menu,
                                           @PathVariable("menuNo") Long menuNo,
                                           @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {

    Menu updatedMenu = menuService.findOne(menuNo);

    Member loginUser = (Member) session.getAttribute("loginUser");
    Long memberId = loginUser.getMemberNo();

    // Member ID를 사용하여 관련된 Store를 찾습니다.
    Store store = entityManager.createQuery("SELECT s FROM Store s WHERE s.memberNo.memberNo = :memberId", Store.class)
            .setParameter("memberId", memberId)
            .getSingleResult();

    if (menu.getStoreNo() == store) {

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
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다.");
    }
  }
}
