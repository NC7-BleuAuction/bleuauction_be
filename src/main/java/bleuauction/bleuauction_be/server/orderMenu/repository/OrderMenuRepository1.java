package bleuauction.bleuauction_be.server.orderMenu.repository;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

import java.util.List;


public interface OrderMenuRepository1 extends JpaRepository<OrderMenu, Long> {

  public OrderMenuDTO save(OrderMenuDTO orderMenuDTO);

  public OrderMenu findByOrderMenuNo(Long orderMenuNo);






}
