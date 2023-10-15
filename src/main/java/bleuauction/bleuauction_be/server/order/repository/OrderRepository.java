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

  public List<Order> findOrderbyMemberNo(Long memberNo) {
    return em.createQuery("SELECT o\n" +
            "FROM Order o\n" +
            "WHERE o.orderNo IN (\n" +
            "    SELECT om.orderNo\n" +
            "    FROM OrderMenu om\n" +
            "    WHERE om.memberNo = :memberNo\n" +
            ")").getResultList();
  }
}
