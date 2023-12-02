package bleuauction.bleuauction_be.server.order.controller;

import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

  private final MemberService memberService;
  private final OrderService orderService;
  private final OrderRepository orderRepository;
  private final CreateJwt createJwt;

  //등록
  @PostMapping("/new")
  public ResponseEntity<?> addorder(Order order, HttpSession session) throws Exception{
    log.info("order/postnew");
    session.setAttribute("order", order);
    return orderService.addOrder(order);
  }


  //회원별 주문 조회
  @GetMapping
  public ResponseEntity<?> findOrders(@RequestHeader("Authorization") String authorizationHeader) {
    createJwt.verifyAccessToken(authorizationHeader);

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    log.info("token: " + tokenMember);

    return orderService.findOrdersByMemberNo(tokenMember.getMemberNo());
  }

  // 가게(가게주인)별 주문 조회
  @GetMapping("/store")
  public ResponseEntity<?> findOrdersbyStore(@RequestHeader("Authorization") String  authorizationHeader) {
    createJwt.verifyAccessToken(authorizationHeader);

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
