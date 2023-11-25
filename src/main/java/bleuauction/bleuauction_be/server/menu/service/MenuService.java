package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final AttachService attachService;
  private final NcpObjectStorageService ncpObjectStorageService;

  //등록
  @Transactional
  public Long enroll(Menu menu, Store store, List<MultipartFile> multipartFiles) {

    menu.setStoreNo(store);
    menuRepository.save(menu);

    if (multipartFiles != null && !multipartFiles.isEmpty()) {
      for (MultipartFile multipartFile : multipartFiles) {
        if (multipartFile.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                  "bleuauction-bucket", "menu/", multipartFile);
          menu.addAttach(attach);
        }
      }
    }
    return menu.getMenuNo();
  }

  public List<Menu> findMenusByStoreNo(Long storeNo) {
    try {
      return menuRepository.findMenusByStoreNoAndMenuStatus(storeNo, MenuStatus.Y);
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public List<Menu> findMenusByStoreNoAndStatus(Long storeNo, MenuStatus menuStatus){
    try {
      return menuRepository.findMenusByStoreNoAndMenuStatus(storeNo, menuStatus);
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }


  //메뉴 1건 조회
  @Transactional
  public Menu findOne(Long menuNo) {
    return menuRepository.findMenusByMenuNo(menuNo);
  }

  //메뉴 삭제(N)
  @Transactional
  public void deleteMenuByMenuNoAndStore(Long menuNo, Long storeNo) {
    Menu menu = menuRepository.findMenusByMenuNo(menuNo);

    if (menu != null && menu.getStoreNo().equals(storeNo)) {
      for (Attach attach : menu.getMenuAttaches()) {
        attachService.changeFileStatusToDeleteByFileNo(attach.getFileNo());
      }
      menu.delete();
    } else {
      throw new IllegalArgumentException("Invalid menu number or store");
    }
  }

  //메뉴 수정
  @Transactional
  public Menu update(Menu updatedMenu, List<MultipartFile> multipartFiles, Long store) {
    Menu existingMenu = menuRepository.findMenusByMenuNo(updatedMenu.getMenuNo());

    if (existingMenu.getStoreNo().equals(store)) {
      existingMenu.setMenuName(updatedMenu.getMenuName());
      existingMenu.setMenuSize(updatedMenu.getMenuSize());
      existingMenu.setMenuPrice(updatedMenu.getMenuPrice());
      existingMenu.setMenuContent(updatedMenu.getMenuContent());

      if (multipartFiles != null && !multipartFiles.isEmpty()) {
        List<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "menu/", multipartFile);
            existingMenu.addAttach(attach);
          }
        }
      }

      return menuRepository.save(existingMenu);
    } else {
      throw new IllegalArgumentException("수정 권한이 없습니다.");
    }
  }


}
