package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final NcpObjectStorageService ncpObjectStorageService;

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
  public Menu update(Menu menu) {
    Menu updatemenu = menuRepository.findOne(menu.getMenuNo());

    updatemenu.setMenuName(menu.getMenuName());
    updatemenu.setMenuSize(menu.getMenuSize());
    updatemenu.setMenuPrice(menu.getMenuPrice());
    updatemenu.setMenuContent(menu.getMenuContent());
    Menu update = menuRepository.save(updatemenu);

    return updatemenu;
  }

}
