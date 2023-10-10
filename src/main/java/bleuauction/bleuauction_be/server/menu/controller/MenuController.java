package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.EntityManager;
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
  public String menu(@Valid MenuForm form,@RequestParam("menuImage") MultipartFile menuImage) {
    Menu menu = new Menu();
    Store storeNo = entityManager.find(Store.class, 1L);
    menu.setStoreNo(storeNo);//테스트용 1번가게
    menu.setMenuName(form.getMenuName());
    menu.setMenuSize(form.getMenuSize());
    menu.setMenuPrice(form.getMenuPrice());
    menu.setMenuContent(form.getMenuContent());
    menu.setMenuStatus(MenuStatus.Y);

    if (menuImage != null && menuImage.getSize() > 0) {
      Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
              "bleuauction-bucket", "menu/", menuImage);
      menu.addAttach(attach);
    }


    menu=entityManager.merge(menu);
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
    menuService.deleteMenu(menuNo);
    return "redirect:/menulist";
  }

  //수정
  @GetMapping("/menu/detail/{menuNo}")
  public String detailMenu(@PathVariable("menuNo") Long menuNo, Model model) {
    Menu menu = menuService.findOne(menuNo);

    // Attach 객체를 가져오거나 생성하여 filePath를 설정합니다.
    // 예를 들어, Menu 객체에 Attach 정보가 있을 경우:
    Attach attach = menu.getMenuAttaches().isEmpty() ? null : menu.getMenuAttaches().get(0);


    model.addAttribute("menu", menu);
    model.addAttribute("attach", attach);

    return "/menus/detail";
  }

  @PostMapping("/menu/update/{menuNo}")
  public String updateMenu(
          @PathVariable("menuNo") Long menuNo,
          @ModelAttribute("menu") @Valid MenuForm form,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "menus/detail";
    }

    menuService.update(menuNo, form.getMenuName(), form.getMenuSize(), form.getMenuPrice(), form.getMenuContent());
    redirectAttributes.addFlashAttribute("successMessage", "메뉴가 수정되었습니다.");
    return "redirect:/menulist";
  }

}
