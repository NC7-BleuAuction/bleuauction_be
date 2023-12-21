package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;


import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class MenuComponentService {
    private final MenuRepository menuRepository;
    private final MenuModuleService menuModuleService;
    private final AttachComponentService attachComponentService;

    //등록
    public Long enroll(Menu menu, Store store, List<MultipartFile> multipartFiles) {

        menu.setStoreNo(store);
        menuModuleService.save(menu);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream().filter(multipartFile -> multipartFile.getSize() > 0)
                    .forEach(multipartFile -> attachComponentService.saveWithMenu(menu, FileUploadUsage.MENU, multipartFile));
        }
        return menu.getId();
    }


    //메뉴 삭제(N)
    public void deleteMenuByMenuNoAndStore(Long menuNo, Store store) {
        Menu menu = menuRepository.findMenusByMenuNo(menuNo);
        if (menu == null || !menu.getStoreNo().equals(store)) {
            throw new IllegalArgumentException("메뉴와 가게 정보가 유효하지 않습니다.");
        }

        for (Attach attach : menu.getAttachs()) {
            attachComponentService.changeFileStatusDeleteByFileNo(attach.getId());
        }
        menu.delete();
    }

    //메뉴 수정
    public Menu update(long menuNo, List<MultipartFile> multipartFiles, Store store) {
        Menu updatedMenu = menuModuleService.findOne(menuNo);
        Menu existingMenu = menuModuleService.findOne(updatedMenu.getId());

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
        return menuModuleService.save(existingMenu);

    }

}
