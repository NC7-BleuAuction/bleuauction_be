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

  public void save(Order order) {
    em.persist(order);
  }

//  public Order findByStoreNo(Long storeNo) {
//    return em.find(Order.class, storeNo);
//  }
//
//  public Order findByMemberNo(Long memberNo) {
//    return em.find(Order.class, memberNo);
//  }

  public List<Order> findByMember(Long memberNo) {
    String jpql = "select o from Order o where o.member.memberNo = :memberNo";
    List<Order> result = em.createQuery(jpql, Order.class)
            .setParameter("memberNo", memberNo)
            .getResultList();
    return result;
  }

  public List<Order> findByStore(Long storeNo) {
    String jpql = "select o from Order o where o.store.storeNo = :storeNo";
    List<Order> result = em.createQuery(jpql, Order.class)
            .setParameter("storeNo", storeNo)
            .getResultList();
    return result;
  }

}
