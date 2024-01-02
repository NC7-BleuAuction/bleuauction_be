package bleuauction.bleuauction_be.server.order.service;//package bleuauction.bleuauction_be.server.order.service;

import static bleuauction.bleuauction_be.server.order.entity.OrderType.T;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderModuleServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderModuleService orderModuleService;


    private final String TEST_MAIL = "test@test.com";
    private final String TEST_PWD = "testpassword123!";
    private final String TEST_NAME = "테스트 이름";

    @Test
    void testEnroll() throws Exception {
        // Given
        Order mockOrder =
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

        // When
        ResponseEntity<?> responseEntity = orderModuleService.addOrder(mockOrder);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Order created successfully", responseEntity.getBody());
    }


    @Test
    @DisplayName("주문 삭제")
    void testDeleteOrder() {
        // Given
        Order mockOrder =
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
        mockOrder.setId(2500L);

        // findOne 메서드가 mockOrder를 반환하도록 설정
        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        // When
        orderModuleService.deleteOrder(mockOrder.getId());

        // Then
        // mockNotice의 status가 N인지 확인
        assertEquals(OrderStatus.N, mockOrder.getOrderStatus());
    }
}