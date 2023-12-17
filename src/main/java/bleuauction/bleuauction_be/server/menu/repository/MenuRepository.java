package bleuauction.bleuauction_be.server.menu.repository;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  Menu findMenusByMenuNo(Long menuNo);

  @Query("SELECT m FROM Menu m WHERE m.storeNo.storeNo = :storeNo AND m.menuStatus = :menuStatus")
  List<Menu> findMenusByStoreNoAndMenuStatus(Long storeNo, MenuStatus menuStatus);
}
