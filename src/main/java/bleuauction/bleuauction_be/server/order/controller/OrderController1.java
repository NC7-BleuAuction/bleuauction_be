package bleuauction.bleuauction_be.server.order.controller;

import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository1;
import bleuauction.bleuauction_be.server.order.service.OrderService1;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order1")
public class OrderController1 {

  private final MemberService memberService;
  private final OrderService1 orderService;
  private final OrderRepository1 orderRepository;
  private final CreateJwt createJwt;

  //등록
  @GetMapping("/new")
  public Order order() {
    Order order = new Order();
    return order;
  }


  @PostMapping("/new")
  public ResponseEntity<?> addorder(Order order, HttpSession session) throws Exception{
    log.info("order/postnew");
    session.setAttribute("order", order);
    return orderService.addOrder(order);
  }


  //회원별 주문 조회
  @GetMapping
  public ResponseEntity<?> findOrders(@RequestHeader("Authorization") String authorizationHeader) {

    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);

    return orderService.findOrdersByMemberNo(tokenMember.getMemberNo());
  }

  // 가게(가게주인)별 주문 조회
  @GetMapping("/store")
  public ResponseEntity<?> findOrdersbyStore(@RequestHeader("Authorization") String  authorizationHeader,HttpSession session) {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);

    return orderService.findOrdersByMemberAndStore(tokenMember.getMemberNo());
  }

  // 삭제--오더메뉴도 같이
  @PostMapping("/delete/{orderNo}")
  public ResponseEntity<String> deleteOrder(@PathVariable("orderNo") Long orderNo) {
      return orderService.deleteOrder(orderNo);
  }

  //디테일(수정)
  @GetMapping("detail/{orderNo}")
  public ResponseEntity<?> detailOrder(HttpSession session, @PathVariable("orderNo") Long orderNo) {
    Optional<Order> order = orderService.findOne(orderNo);
    session.setAttribute("order", order);
    return ResponseEntity.ok(order);
  }

  @PutMapping("/update/{orderNo}")
  public ResponseEntity<String> updateOrder (@PathVariable("orderNo") Long orderNo) {
    log.info("order/update");
    return orderService.update(orderNo);
  }
}
