package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final EntityManager entityManager;

  //등록페이지
  @GetMapping("/menu/new")
  public String createForm(Model model) {
    model.addAttribute("menuForm", new MenuForm());
    log.info("menu/new");
    return "/menus/new";
  }

  //등록처리
  @PostMapping("/menu/new")
  @Transactional
  public String menu(@Valid MenuForm form, MultipartFile[] files) {
    Menu menu = new Menu();
    Store storeNo = entityManager.find(Store.class, 1L);
    menu.setStoreNo(storeNo);//테스트용 1번가게
    menu.setMenuName(form.getMenuName());
    menu.setMenuSize(form.getMenuSize());
    menu.setMenuPrice(form.getMenuPrice());
    menu.setMenuContent(form.getMenuContent());
    menu.setMenuStatus(MenuStatus.Y);

    ArrayList<Attach> attaches = new ArrayList<>();
    for (MultipartFile part : files) {
      if (part.getSize() > 0) {
        Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                "bleuauction-bucket", "menu/", part);
        menu.addAttach(attach);
      }
    }
      menu = entityManager.merge(menu);
      menuService.enroll(menu);
      log.info("menu/postnew");
      return "redirect:/menulist";
    }



  //목록 조회
  @GetMapping("/menulist")
  public String list(Model model) {
    List<Menu> menus = menuService.findmenus();
    model.addAttribute("menus", menus);
    return "/menus/menuList";
  }

  //삭제
  @PostMapping("/menu/delete/{menuNo}")
  public String deleteMenu(@PathVariable("menuNo") Long menuNo) {
    Menu menu = menuService.findOne(menuNo);

    // 사진 상태를 'N'으로 변경
    for (Attach attach : menu.getMenuAttaches()) {
      attachService.update(attach.getFileNo());
    }
    menuService.deleteMenu(menuNo);

    return "redirect:/menulist";
  }

  //사진삭제
  @GetMapping("/menu/deletefile/{menuNo}/{fileNo}")
  public String fileDelete(@PathVariable Long menuNo, @PathVariable Long fileNo, Model model) {
    Menu menu = menuService.findOne(menuNo);
    model.addAttribute("menu", menu);

    Attach attachToDelete = null;
    for (Attach attach : menu.getMenuAttaches()) {
      if (attach.getFileNo().equals(fileNo)) {
        attachToDelete = attach;
        break;
      }
    }

    // 변경된 내용을 저장
    attachService.update(fileNo);

    return "redirect:/menulist";
  }



  //수정
  @GetMapping("/menu/detail/{menuNo}")
  public String detailMenu(@PathVariable("menuNo") Long menuNo, Model model) {
    Menu menu = menuService.findOne(menuNo);

    // 예를 들어, Menu 객체에 Attach 정보가 있을 경우:
    Attach attach = menu.getMenuAttaches().isEmpty() ? null : menu.getMenuAttaches().get(0);

    model.addAttribute("menu", menu);
    model.addAttribute("attach", attach);

    return "/menus/detail";
  }

  @PostMapping("/menu/update/{menuNo}")
  public String updateMenu(
          @PathVariable("menuNo") Long menuNo,
          @ModelAttribute("menu") @Valid MenuForm form, @RequestParam("newMenuImage") MultipartFile newMenuImage) {

    Menu menu = menuService.findOne(menuNo);

    if (newMenuImage != null && newMenuImage.getSize() > 0) {
      Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
              "bleuauction-bucket", "menu/", newMenuImage);
      menu.addAttach(attach);
    }

    menuService.update(menuNo, form.getMenuName(), form.getMenuSize(), form.getMenuPrice(), form.getMenuContent());
    return "redirect:/menulist";
  }

}
