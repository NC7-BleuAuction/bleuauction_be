package bleuauction.bleuauction_be.server.order.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final NcpObjectStorageService ncpObjectStorageService;
  private final OrderService orderService;
  private final OrderRepository orderRepository;

  //등록
  @GetMapping("/api/order/new")
  public Order order() {
    Order order = new Order();
    return order;
  }

  @PostMapping("/api/order/new")
  @Transactional
  public ResponseEntity<String> order(Order order) {
    orderService.enroll(order);
    log.info("order/postnew");
    return ResponseEntity.status(HttpStatus.CREATED).body("Menu created successfully");
  }

  // 회원별 주문 조회
  @GetMapping("/api/order/{memberNo}")
  public ResponseEntity<?> findOrders(HttpSession session) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    List<Order> orders = orderRepository.findOrderbyMemberNo(loginUser.getMemberNo());

    if (orders.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 없습니다.");
    } else {
      return ResponseEntity.ok(orders);
    }
  }

  // 삭제
  @PostMapping("api/order/delete/{orderNo}")
  public ResponseEntity<String> deleteOrder(@PathVariable("orderNo") Long orderNo) {
    Order order = orderService.findOne(orderNo);
    if (order != null) {
      order.delete();
      orderService.enroll(order);
    }
    return ResponseEntity.ok("Order deleted successfully");
  }

  //디테일(수정)
  @GetMapping("/api/order/detail/{orderNo}")
  public ResponseEntity<Order> detailOrder(@PathVariable("orderNo") Long orderNo) {
    Order order = orderService.findOne(orderNo);
    return ResponseEntity.ok(order);
  }

  @PostMapping("/api/order/update/{orderNo}")
  public ResponseEntity<String> updateOrder (Order order, @PathVariable("orderNo") Long orderNo) {
    orderService.update(order);
    log.info("order/update");
    return ResponseEntity.ok("Order updated successfully");
  }
}
