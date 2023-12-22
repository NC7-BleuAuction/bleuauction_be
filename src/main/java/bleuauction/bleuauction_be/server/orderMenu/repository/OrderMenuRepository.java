package bleuauction.bleuauction_be.server.orderMenu.repository;


import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    public OrderMenuDTO save(OrderMenuDTO orderMenuDTO);

    public OrderMenu findByOrderMenuNo(Long orderMenuNo);
}
