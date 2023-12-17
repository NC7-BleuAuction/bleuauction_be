package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final AttachComponentService attachComponentService;

    //등록
    @Transactional
    public Long enroll(Menu menu, Store store, List<MultipartFile> multipartFiles) {

        menu.setStoreNo(store);
        menuRepository.save(menu);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream().filter(multipartFile -> multipartFile.getSize() > 0)
                    .forEach(multipartFile -> attachComponentService.saveWithMenu(menu, FileUploadUsage.MENU, multipartFile));
        }
        return menu.getMenuNo();
    }

    public List<Menu> findMenusByStoreNo(Long storeNo) {
        return menuRepository.findMenusByStoreNoAndMenuStatus(storeNo, MenuStatus.Y);
    }

    public List<Menu> findMenusByStoreNoAndStatus(Long storeNo, MenuStatus menuStatus) {
        return menuRepository.findMenusByStoreNoAndMenuStatus(storeNo, menuStatus);
    }

    //메뉴 1건 조회
    @Transactional
    public Menu findOne(Long menuNo) {
        return menuRepository.findMenusByMenuNo(menuNo);
    }

    //메뉴 삭제(N)
    @Transactional
    public void deleteMenuByMenuNoAndStore(Long menuNo, Store store) {
        Menu menu = menuRepository.findMenusByMenuNo(menuNo);
        if (menu == null || !menu.getStoreNo().equals(store)) {
            throw new IllegalArgumentException("메뉴와 가게 정보가 유효하지 않습니다.");
        }

        for (Attach attach : menu.getMenuAttaches()) {
            attachComponentService.changeFileStatusDeleteByFileNo(attach.getFileNo());
        }
        menu.delete();
    }

    //메뉴 수정
    @Transactional
    public Menu update(Menu updatedMenu, List<MultipartFile> multipartFiles, Store store) {
        Menu existingMenu = menuRepository.findMenusByMenuNo(updatedMenu.getMenuNo());

        if (!existingMenu.getStoreNo().equals(store)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        existingMenu.setMenuName(updatedMenu.getMenuName());
        existingMenu.setMenuSize(updatedMenu.getMenuSize());
        existingMenu.setMenuPrice(updatedMenu.getMenuPrice());
        existingMenu.setMenuContent(updatedMenu.getMenuContent());

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream()
                    .filter(multipartFile -> multipartFile.getSize() > 0)
                    .forEach(multipartFile -> attachComponentService.saveWithMenu(existingMenu, FileUploadUsage.MENU, multipartFile));
        }
        return menuRepository.save(existingMenu);

    }


}
