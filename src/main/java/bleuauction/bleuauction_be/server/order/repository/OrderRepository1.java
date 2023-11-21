package bleuauction.bleuauction_be.server.order.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface OrderRepository1 extends JpaRepository<Order, Long> {


  Optional<Order> findByOrderNo(Long orderNo);



  @Query("SELECT o\n" +
          "FROM Order o\n" +
          "WHERE o.orderNo IN (\n" +
          "    SELECT om.orderNo\n" +
          "    FROM OrderMenu om\n" +
          "    WHERE om.memberNo = :memberNo\n" +
          ")")
  List<Order> findByOrderMemberMemberNo(@Param("memberNo") Member memberNo);

  @Query("SELECT o " +
          "FROM Order o " +
          "JOIN o.OrderMenus om " +
          "JOIN om.menuNo menu " +
          "JOIN menu.storeNo store " +
          "JOIN store.memberNo member " +
          "WHERE o.orderStatus = 'Y' AND member.memberNo = :memberNo")
  List<Order> findOrdersByMemberAndStore(@Param("memberNo") Member memberNo);
}