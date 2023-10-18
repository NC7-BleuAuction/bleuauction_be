package bleuauction.bleuauction_be.server.pay.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.repository.PayRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayService {
    private final PayRepository payRepository;
    private final OrderRepository orderRepository;

    public List<Pay> selectPayList() {
        return payRepository.findAll();
    }
    public Pay insert(PayInsertRequest request, Long orderNo) throws Exception {
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findOne(orderNo));
        Order order = optionalOrder.orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderNo));
        if (request.getOrderStatus() != OrderStatus.N) {
            throw new IllegalAccessException("주문을 완료해야 결제가 가능합니다.");
        }
        Pay pay = request.getPayEntity(order);
        pay.setPayDatetime(request.getPayDatetime());
        pay.setPayCancelDatetime(request.getPayCancelDatetime());
        return payRepository.save(pay);
    }
}
