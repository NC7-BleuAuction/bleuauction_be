package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.exception.OrderNotFoundException;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberService memberService;



  //등록
  public ResponseEntity<?> addOrder(Order order) throws Exception{
    orderRepository.save(order);
    return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
  }


  //주문 1건 조회
  @Transactional(readOnly = true)
  public Optional<Order> findOne(Long orderNo) {
    return orderRepository.findByOrderNo(orderNo);
  }

  //가게 별 주문 조회
  public ResponseEntity<?> findOrdersByMemberAndStore(Long memberNo) {
    Optional<Member> loginUser = memberService.findByMemberNo(memberNo);

    if (!loginUser.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자가 아닙니다.");
    }
    List<Order> orders = orderRepository.findOrdersByMemberAndStore(loginUser.get());

      if (orders.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 없습니다.");
      } else {
        return ResponseEntity.ok(orders);
      }
    }


  //회원 별 주문 조회
  public ResponseEntity<?> findOrdersByMemberNo(Long memberNo) {
    Optional<Member> loginUser = memberService.findByMemberNo(memberNo);

    if (!loginUser.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자가 아닙니다.");
    }

    // Use the custom query from the repository
    List<Order> orders = orderRepository.findByOrderMemberMemberNo(loginUser.get());

    if (orders.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 없습니다.");
    } else {
      // Assuming calculateOrderPrice is a method in the Order class
      orders.forEach(Order::calculateOrderPrice); // 주문 가격 계산
      return ResponseEntity.ok(orders);
    }
  }


  //메뉴 삭제(N)
  public ResponseEntity<String> deleteOrder(@PathVariable("orderNo") Long orderNo) {
    Optional<Order> order = orderRepository.findByOrderNo(orderNo);
    order.get().delete();
    return ResponseEntity.ok("Order deleted successfully");
  }

  //메뉴 수정
  public ResponseEntity<String> update(@PathVariable("orderNo") Long orderNo) {
    Optional<Order> orderOptional = orderRepository.findByOrderNo(orderNo);

    if (orderOptional.isPresent()) {
      Order order = orderOptional.get();

      Optional<Order> updateorder = orderRepository.findByOrderNo(orderNo);

      updateorder.ifPresent(existingOrder -> {
        existingOrder.setOrderType(order.getOrderType());
        // existingOrder.setOrderPrice(order.getOrderPrice()); // 필요하다면 주석 해제
        existingOrder.setOrderRequest(order.getOrderRequest());
        existingOrder.setRecipientPhone(order.getRecipientPhone());
        existingOrder.setRecipientName(order.getRecipientName());
        existingOrder.setRecipientZipcode(order.getRecipientZipcode());
        existingOrder.setRecipientAddr(order.getRecipientAddr());
        existingOrder.setRecipientDetailAddr(order.getRecipientDetailAddr());
      });

      return ResponseEntity.ok("Order updated successfully");
    } else {
      // 지정된 주문 번호를 가진 주문이 찾아지지 않은 경우 처리
      return ResponseEntity.ok("Order is not existed");
    }
  }

  //주문 1건 조회
  @Transactional(readOnly = true)
  public Order findOrderById(Long orderNo) {
    return orderRepository.findById(orderNo)
            .orElseThrow(() -> new OrderNotFoundException(orderNo));
  }

}
