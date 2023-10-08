package bleuauction.bleuauction_be.server.item.repository;

import bleuauction.bleuauction_be.server.item.entity.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;

  public void save(Item item) {
    em.persist((item));
  }

  public Item findOne(Long itemNo) {
    return em.find(Item.class, itemNo);
  }

  public List<Item> findAll() {
    List<Item> result = em.createQuery("select i from Item i", Item.class)
            .getResultList();
    return result;
  }
}
