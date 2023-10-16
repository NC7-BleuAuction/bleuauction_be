package bleuauction.bleuauction_be.server.order.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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

//  public List<Order> findByOrderMenusMemberMemberNo(@Param("memberNo") Long memberNo) {
//    return em.createQuery("SELECT o\n" +
//            "FROM Order o\n" +
//            "WHERE o.orderNo IN (\n" +
//            "    SELECT om.orderNo\n" +
//            "    FROM OrderMenu om\n" +
//            "    WHERE om.memberNo = :memberNo\n" +
//            ")").getResultList();
//  }
public List<Order> findByOrderMenusMemberMemberNo(@Param("memberNo") Member memberNo) {
  return em.createQuery("SELECT o\n" +
                  "FROM Order o\n" +
                  "WHERE o.orderNo IN (\n" +
                  "    SELECT om.orderNo\n" +
                  "    FROM OrderMenu om\n" +
                  "    WHERE om.memberNo = :memberNo\n" +
                  ")").setParameter("memberNo", memberNo) // named parameter를 설정
          .getResultList();
}
}
