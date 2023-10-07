package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  //등록
  @Transactional
  public Long enroll(Menu menu) {
    menuRepository.save(menu);
    return menu.getMenuNo();
  }

  //메뉴 전체 조회
  @Transactional(readOnly = true)
  public List<Menu> findmenus() {
    return menuRepository.findAll();
  }

  //메뉴 1건 조회
  @Transactional(readOnly = true)
  public Menu findOne(Long menuNo) {
    return menuRepository.findOne(menuNo);
  }

  //메뉴 삭제(N)
  @Transactional
  public void deleteMenu(Long menuNo) {
    Menu menu = menuRepository.findOne(menuNo);
    menu.delete();
  }

  //메뉴 수정
  @Transactional
  public Menu update(Long menuNo, String menuName, MenuSize menuSize, String menuPrice,String menuContent) {
    Menu menu = menuRepository.findOne(menuNo);
    menu.setMenuName(menuName);
    menu.setMenuSize(menuSize);
    menu.setMenuPrice(menuPrice);
    menu.setMenuContent(menuContent);
    return menu;
  }

}
