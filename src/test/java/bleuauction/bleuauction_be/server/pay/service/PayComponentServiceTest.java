package bleuauction.bleuauction_be.server.pay.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.entity.OrderType;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.entity.PayStatus;
import bleuauction.bleuauction_be.server.pay.entity.PayType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PayComponentServiceTest {
    @Mock
    private PayModuleService payModuleService;

    @InjectMocks
    private PayComponentService payComponentService;


    @Test
    @DisplayName("결제정보를 생성 할 때, 파라미터로 받은 PayInsertRequest의 orderStatus가 N인 경우 RuntimeException이 발생한다.")
    void createPayment_WhenPayInsertRequestInOrderStatusIsN_ThrowRuntimeException() {
        // given
        Long orderNo = 1L;
        PayInsertRequest payInsertRequest = new PayInsertRequest();
        payInsertRequest.setOrderNo(1L);
        payInsertRequest.setPayType(PayType.C);
        payInsertRequest.setPayPrice(80000);
        payInsertRequest.setPayStatus(PayStatus.Y);
        payInsertRequest.setOrderStatus(OrderStatus.N);

        // when && then
        RuntimeException e = assertThrows(RuntimeException.class, () -> payComponentService.createPayment(payInsertRequest, null));
        assertEquals("주문을 완료해야 결제가 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("결제 정보를 생성 할 때, 주문이 완료된 상태인 경우, 결제정보가 생성된다")
    void createPayment_WhenOrderStatusIsY_ThenCreatePay() {
        // given
        Order mockOrder = new Order();
        mockOrder.setOrderNo(1L);
        mockOrder.setOrderType(OrderType.Q);
        mockOrder.setOrderPrice(50000);
        mockOrder.setOrderRequest("매운맛으로 부탁드립니다.");
        mockOrder.setRecipientName("뭐가 들어가는지 모르겠어요");
        mockOrder.setRecipientPhone("010-1111-1111");
        mockOrder.setRecipientZipcode("12345");
        mockOrder.setRecipientAddr("서울특별시 강남의");
        mockOrder.setRecipientDetailAddr("빌딩 5층일까 7층일까");
        mockOrder.setRegDatetime(Timestamp.valueOf(LocalDateTime.now()));
        mockOrder.setMdfDatetime(Timestamp.valueOf(LocalDateTime.now()));
        mockOrder.setOrderStatus(OrderStatus.Y);

        PayInsertRequest payInsertRequest = new PayInsertRequest();
        payInsertRequest.setOrderNo(mockOrder.getOrderNo());
        payInsertRequest.setPayPrice(mockOrder.getOrderPrice());
        payInsertRequest.setOrderType(mockOrder.getOrderType());
        payInsertRequest.setOrderPrice(mockOrder.getOrderPrice());
        payInsertRequest.setPayStatus(PayStatus.Y);
        payInsertRequest.setPayDatetime(Timestamp.valueOf(LocalDateTime.now()));

        Pay pay = payInsertRequest.getPayEntity(mockOrder);
        pay.setPayNo(1L);

        given(payModuleService.save(any(Pay.class))).willReturn(pay);

        // when
        Pay createdPay = payComponentService.createPayment(payInsertRequest, null);

        // then
        assertEquals(pay, createdPay);
        inOrder(payModuleService).verify(payModuleService, times(1)).save(any(Pay.class));
    }

}