package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class MenuModuleService {

    private final MenuRepository menuRepository;

    public List<Menu> findMenusByStoreNo(Long storeNo) {
        return menuRepository.findMenusByStoreNoAndMenuStatus(storeNo, MenuStatus.Y);
    }


    //메뉴 1건 조회
    public Menu findOne(Long menuNo) {
        return menuRepository.findMenusByMenuNo(menuNo);
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }
}
