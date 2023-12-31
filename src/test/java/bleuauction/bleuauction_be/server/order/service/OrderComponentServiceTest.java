package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static bleuauction.bleuauction_be.server.order.entity.OrderType.T;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderComponentServiceTest {

    @Mock
    private OrderModuleService orderModuleService;

    @InjectMocks private OrderComponentService orderComponentService;

    @Test
    @DisplayName("주문 수정")
    void testUpdateOrder() {
        // given
        Order existingOrder =
                Order.builder()
                        .orderType(T)
                        .orderPrice(10000)
                        .orderRequest("많이 주세요")
                        .recipientPhone("010-3499-4444")
                        .recipientName("박승현")
                        .recipientZipcode("9999")
                        .recipientAddr("서울시 강남구")
                        .recipientDetailAddr("502-1호")
                        .build();
        existingOrder.setId(2500L);

        // findOne 호출될 때 existingOrder를 리턴하도록 설정
        when(orderRepository.findById(existingOrder.getId()))
                .thenReturn(Optional.of(existingOrder));

        // 업데이트할 내용을 담은 새로운 Order 객체 생성
        Order updatedOrder =
                Order.builder()
                        .orderType(T)
                        .orderPrice(10000)
                        .orderRequest("수정 요청")
                        .recipientPhone("010-3499-4444")
                        .recipientName("박승현")
                        .recipientZipcode("9999")
                        .recipientAddr("수정주소")
                        .recipientDetailAddr("502-1호")
                        .build();
        updatedOrder.setId(existingOrder.getId()); // 기존의 orderNo를 설정

        // when
        // 서비스의 update 메소드 호출
        ResponseEntity<String> responseEntity = orderComponentService.update();

        // then
        // 반환된 ResponseEntity의 상태 코드와 메시지를 확인
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Order updated successfully", responseEntity.getBody());
    }


}