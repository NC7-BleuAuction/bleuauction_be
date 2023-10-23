package bleuauction.bleuauction_be.server.orderMenu.repository;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderMenuRepository {

  private final EntityManager em;
  private final OrderRepository orderRepository;

  public OrderMenu save(OrderMenu orderMenu) {
    em.persist(orderMenu);
    return orderMenu;
  }

  public OrderMenuDTO save(OrderMenuDTO orderMenuDTO) {
    em.persist(orderMenuDTO);
    return orderMenuDTO;
  }

  public OrderMenu findOne(Long orderMenuNo) {
    return em.find(OrderMenu.class, orderMenuNo);
  }



  public List<OrderMenu> findAll() {
    List<OrderMenu> result = em.createQuery("select om from OrderMenu om", OrderMenu.class)
            .getResultList();
    return result;
  }
}
