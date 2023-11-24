package bleuauction.bleuauction_be.server.menu.repository;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository1 extends JpaRepository<Menu, Long> {
    public Menu findMenusByMenuNo(Long menuNo);
}
