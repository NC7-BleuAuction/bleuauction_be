package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
