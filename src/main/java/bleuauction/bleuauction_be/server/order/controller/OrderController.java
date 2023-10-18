package bleuauction.bleuauction_be.server.order.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final OrderRepository orderRepository;

  //등록
  @GetMapping("/api/order/new")
  public Order order() {
    Order order = new Order();
    return order;
  }

  @Transactional
  @PostMapping("/api/order/new")
  public ResponseEntity<String> order(Order order, HttpSession session) {
    orderService.enroll(order);
    session.setAttribute("order", order);
    log.info("order/postnew");
    return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
  }

  // 회원별 주문 조회
  @GetMapping("/api/order")
  public ResponseEntity<?> findOrders(HttpSession session) {
    Member loginUser = (Member) session.getAttribute("loginUser");

    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자가 아닙니다.");
    }

    List<Order> orders = orderRepository.findByOrderMenusMemberMemberNo(loginUser);

    if (orders.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 없습니다.");
    } else {
      return ResponseEntity.ok(orders);
    }
  }

  // 삭제--오더메뉴도 같이
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
  public ResponseEntity<Order> detailOrder(HttpSession session, @PathVariable("orderNo") Long orderNo) {
    Order order = orderService.findOne(orderNo);
    session.setAttribute("order", order);
    return ResponseEntity.ok(order);
  }

  @PostMapping("/api/order/update/{orderNo}")
  public ResponseEntity<String> updateOrder (Order order, @PathVariable("orderNo") Long orderNo) {
    orderService.update(order);
    log.info("order/update");
    return ResponseEntity.ok("Order updated successfully");
  }
}
