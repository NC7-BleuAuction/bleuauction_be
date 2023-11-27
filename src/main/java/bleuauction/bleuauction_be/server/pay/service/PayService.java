package bleuauction.bleuauction_be.server.pay.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.exception.PayHistoryNotFoundException;
import bleuauction.bleuauction_be.server.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayService {
    private final PayRepository payRepository;

    /**
     * 결제 정보를 조회하며,
     * 존재하지 않는 history인 경우에는 PayHistoryNotFoundException이 발생한다.
     * @param payNo
     * @return
     */
    public Pay getPay(Long payNo) {
        return payRepository.findById(payNo)
                .orElseThrow(() -> new PayHistoryNotFoundException(payNo));
    }

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
        return payRepository.save(request.getPayEntity(order));
    }
}
