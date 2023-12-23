package bleuauction.bleuauction_be.server.orderMenu.repository;


import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    //    OrderMenuDTO save(OrderMenuDTO orderMenuDTO);
}
