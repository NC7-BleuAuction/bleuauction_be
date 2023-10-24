package bleuauction.bleuauction_be.server.order.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final MemberService memberService;
  private final OrderService orderService;
  private final OrderRepository orderRepository;
  private final CreateJwt createJwt;

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


  //회원별 주문 조회
  @GetMapping("/api/order")
  public ResponseEntity<?> findOrders(@RequestHeader("Authorization") String  authorizationHeader) {

    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자가 아닙니다.");
    }

    List<Order> orders = orderRepository.findByOrderMenusMemberMemberNo(loginUser.get());

    if (orders.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 없습니다.");
    } else {
      for (Order order : orders) {
        order.calculateOrderPrice(); // 주문 가격 계산
      }
      return ResponseEntity.ok(orders);
    }
  }

  // 가게(가게주인)별 주문 조회
  @GetMapping("/api/store/order")
  public ResponseEntity<?> findOrdersbyStore( @RequestHeader("Authorization") String  authorizationHeader,HttpSession session) {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Long memberNo = loginUser.get().getMemberNo();


    if (loginUser.get() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자가 아닙니다.");
    }

    List<Order> orders = orderRepository.findOrdersByMemberAndStore(memberNo);

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
