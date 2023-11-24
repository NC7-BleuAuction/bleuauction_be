package bleuauction.bleuauction_be.server.menu.repository;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
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

  public List<Menu> findMenusByStoreNoAndStatus(Long storeNo, MenuStatus menuStatus) {
    String jpql = "SELECT m FROM Menu m WHERE m.storeNo.storeNo = :storeNo AND m.menuStatus = :menuStatus";
    TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
    query.setParameter("storeNo", storeNo);
    query.setParameter("menuStatus", menuStatus);
    List<Menu> result = query.getResultList();
    return result;
  }

  public List<Menu> findAll() {
    String jpql = "SELECT m FROM Menu m WHERE m.menuStatus = :menuStatus";
    TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
    query.setParameter("menuStatus", MenuStatus.Y);
    List<Menu> result = query.getResultList();
    return result;
  }

  public Menu findMenusByMenuNo(Long menuNo) {
    return em.find(Menu.class, menuNo);
  }

//  public List<Menu> findAll() {
//    List<Menu> result = em.createQuery("select m from Menu m", Menu.class)
//            .getResultList();
//    return result;
//  }



}
