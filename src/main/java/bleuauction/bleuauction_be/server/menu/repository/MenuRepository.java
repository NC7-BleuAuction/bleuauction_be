package bleuauction.bleuauction_be.server.menu.repository;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

  private final EntityManager em;

  public Menu save(Menu menu) {
    em.persist(menu);
    return menu;
  }

  public Menu findOne(Long menuNo) {
    return em.find(Menu.class, menuNo);
  }

  public List<Menu> findMenusByStoreNo(Long storeNo) {
    String jpql = "SELECT m FROM Menu m WHERE m.storeNo.storeNo = :storeNo";
    TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
    query.setParameter("storeNo", storeNo);
    List<Menu> result = query.getResultList();
    return result;
  }


  public List<Menu> findAll() {
    List<Menu> result = em.createQuery("select m from Menu m", Menu.class)
            .getResultList();
    return result;
  }

  public List<Menu> findByMenuStatus(MenuStatus status) {
    List<Menu> result = em.createQuery("select m from Menu n where m.menuStatus = :status", Menu.class)
            .setParameter("status", status)
            .getResultList();
    return result;
  }
}
