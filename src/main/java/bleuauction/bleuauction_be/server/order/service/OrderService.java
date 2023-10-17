package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  //등록
  @Transactional
  public Long enroll(Order order) {
    orderRepository.save(order);
    return order.getOrderNo();
  }

  //주문 전체 조회
  @Transactional(readOnly = true)
  public List<Order> findOrders() {
    return orderRepository.findAll();
  }

  //주문 1건 조회
  @Transactional(readOnly = true)
  public Order findOne(Long orderNo) {
    return orderRepository.findOne(orderNo);
  }


  //메뉴 삭제(N)
  public void deleteOrder(Long orderNo) {
    Order order = orderRepository.findOne(orderNo);
    order.delete();
  }

  //메뉴 수정
  @Transactional
  public Order update(Order order) {
    Order updateorder = orderRepository.findOne(order.getOrderNo());

    updateorder.setOrderType(order.getOrderType());
    updateorder.setOrderPrice(order.getOrderPrice());
    updateorder.setOrderRequest(order.getOrderRequest());
    updateorder.setRecipientPhone(order.getRecipientPhone());
    updateorder.setRecipientName(order.getRecipientName());
    updateorder.setRecipientZipcode(order.getRecipientZipcode());
    updateorder.setRecipientAddr(order.getRecipientAddr());
    updateorder.setRecipientDetailAddr(order.getRecipientDetailAddr());
    orderRepository.save(updateorder);

    return updateorder;
  }
}
