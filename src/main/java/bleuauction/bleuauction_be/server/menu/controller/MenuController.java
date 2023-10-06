package bleuauction.bleuauction_be.server.menu.controller;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.service.MenuService;
import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;
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
  public String menu(@Valid MenuForm form) {
    Menu menu = new Menu();
    Store store = entityManager.find(Store.class, 1L);
    menu.setStore(store);//테스트용 1번가게
    menu.setMenuName(form.getMenuName());
    menu.setMenuSize(form.getMenuSize());
    menu.setMenuPrice(form.getMenuPrice());
    menu.setMenuContent(form.getMenuContent());
    menu.setMenuStatus(MenuStatus.Y);
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
    model.addAttribute("menu", menu);
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

    return "redirect:/menulist";
  }

}
