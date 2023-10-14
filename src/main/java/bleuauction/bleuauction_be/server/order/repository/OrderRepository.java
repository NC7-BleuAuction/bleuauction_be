package bleuauction.bleuauction_be.server.order.repository;

import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public Order save(Order order) {
    em.persist(order);
    return order;
  }

  public Order findOne(Long orderNo) {
    return em.find(Order.class, orderNo);
  }

  public List<Order> findAll() {
    List<Order> result = em.createQuery("select o from Order o", Order.class)
            .getResultList();
    return result;
  }
}
