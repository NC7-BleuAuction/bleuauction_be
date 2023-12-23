package bleuauction.bleuauction_be.server.menu.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.exception.MenuNotFoundException;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class MenuModuleService {

    private final MenuRepository menuRepository;

    public List<Menu> findAllByStore(Store store) {
        return menuRepository.findAllByStoreAndStatus(store, MenuStatus.Y);
    }

    // 메뉴 1건 조회
    public Menu findOne(Long menuNo) {
        return menuRepository.findById(menuNo).orElseThrow(() -> MenuNotFoundException.EXCEPTION);
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }
}
