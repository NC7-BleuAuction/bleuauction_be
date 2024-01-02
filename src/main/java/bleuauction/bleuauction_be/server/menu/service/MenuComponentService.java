package bleuauction.bleuauction_be.server.menu.service;


import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.menu.dto.MenuDTO;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.exception.MenuNotFoundException;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class MenuComponentService {
    private final MenuRepository menuRepository;
    private final MenuModuleService menuModuleService;
    private final AttachComponentService attachComponentService;


    // 등록
    public Long enroll(Menu menu, Store store, List<MultipartFile> multipartFiles) {

        menu.addStore(store);
        menuModuleService.save(menu);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream()
                    .filter(multipartFile -> multipartFile.getSize() > 0)
                    .forEach(
                            multipartFile ->
                                    attachComponentService.saveWithMenu(
                                            menu, FileUploadUsage.MENU, multipartFile));
        }
        return menu.getId();
    }

    // 메뉴 삭제(N)
    public void deleteMenuByMenuNoAndStore(Long menuId, Store store) {
        menuRepository
                .findById(menuId)
                .ifPresentOrElse(
                        menu -> {
                            if (!menu.getStore().equals(store)) {
                                throw new IllegalArgumentException("메뉴와 가게 정보가 유효하지 않습니다.");
                            }
                            menu.delete();
                        },
                        () -> {
                            throw MenuNotFoundException.EXCEPTION;
                        });
    }

    // 메뉴 수정
    // TODO : 로직이 어구가 맞지 않네요, 수정필요합니다. 아니면 파일만 수정하는건가요?
    public Menu update(Long menuId, List<MultipartFile> multipartFiles, Store store, MenuDTO request) {
        Menu existingMenu = menuModuleService.findOne(menuId);

        if (!existingMenu.getStore().equals(store)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        existingMenu.setName(request.getName());
        existingMenu.setSize(request.getSize());
        existingMenu.setPrice(request.getPrice());
        existingMenu.setContent(request.getContent());

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream()
                    .filter(multipartFile -> multipartFile.getSize() > 0)
                    .forEach(
                            multipartFile ->
                                    attachComponentService.saveWithMenu(
                                            existingMenu, FileUploadUsage.MENU, multipartFile));
        }
        return menuModuleService.save(existingMenu);
    }
}
