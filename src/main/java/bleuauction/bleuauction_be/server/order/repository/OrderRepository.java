package bleuauction.bleuauction_be.server.order.repository;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMemberOrderByRegDatetimeDesc(Member member);

    List<Order> findAllByStoreAndOrderStatusOrderByRegDatetimeDesc(Store store, OrderStatus status);

    List<Order> findAllByMemberAndOrderStatusOrderByRegDatetimeDesc(
            Member member, OrderStatus status);
}
