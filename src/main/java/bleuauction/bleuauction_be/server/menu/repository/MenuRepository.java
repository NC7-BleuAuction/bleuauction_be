package bleuauction.bleuauction_be.server.menu.repository;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

  private final EntityManager em;

  public void save(Menu menu) {
    em.persist(menu);
  }

  public Menu findOne(Long menuNo) {
    return em.find(Menu.class, menuNo);
  }

  public List<Menu> findAll() {
    List<Menu> result = em.createQuery("select m from Menu m", Menu.class)
            .getResultList();
    return result;
  }
}
