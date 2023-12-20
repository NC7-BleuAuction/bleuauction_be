package bleuauction.bleuauction_be.server.pay.service;

import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ComponentService
@RequiredArgsConstructor
@Transactional
public class PayComponentService {
    private final PayModuleService payModuleService;

    /**
     * 사용자가 결제한 결제 정보를 Insert한다.
     *
     * @param request
     * @param order
     * @return
     */
    public Pay createPayment(PayInsertRequest request, Order order) {
        // [TODO] : 여기 PayStatus도 Y여야 하는거 아닌가..?
        if (OrderStatus.N.equals(request.getOrderStatus())) {
            throw new RuntimeException("주문을 완료해야 결제가 가능합니다.");
        }
        return payModuleService.save(request.getPayEntity(order));
    }
}
