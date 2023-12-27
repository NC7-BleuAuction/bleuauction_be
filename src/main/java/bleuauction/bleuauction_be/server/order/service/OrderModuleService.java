package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.exception.OrderNotFoundException;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class OrderModuleService {

    private OrderRepository orderRepository;

    // 등록
    public ResponseEntity<?> addOrder(Order order) {
        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }


    // 주문 1건 조회
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long orderNo) {
        return orderRepository.findById(orderNo);
    }



    // 주문 1건 조회
    @Transactional(readOnly = true)
    public Order findOrderById(Long orderNo) {
        return orderRepository
                .findById(orderNo)
                .orElseThrow(() -> new OrderNotFoundException(orderNo));
    }


    // 메뉴 삭제(N)
    public ResponseEntity<String> deleteOrder(Long orderNo) {
        Order order =
                orderRepository
                        .findById(orderNo)
                        .orElseThrow(() -> new OrderNotFoundException(orderNo));
        order.deleteOrder();
        return ResponseEntity.ok("Order deleted successfully");
    }


}
