package bleuauction.bleuauction_be.server.order.service;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static bleuauction.bleuauction_be.server.order.entity.OrderType.T;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MemberService memberService;

    @Test
    void testEnroll() throws Exception{
        // Given
        Order mockOrder = new Order();
        mockOrder.setOrderType(T);
        mockOrder.setOrderPrice(10000);
        mockOrder.setOrderRequest("많이 주세요");
        mockOrder.setRecipientPhone("010-3499-4444");
        mockOrder.setRecipientName("박승현");
        mockOrder.setRecipientZipcode("9999");
        mockOrder.setRecipientAddr("서울시 강남구");
        mockOrder.setRecipientDetailAddr("502-1호");

        // When
        ResponseEntity<?> responseEntity = orderService.addOrder(mockOrder);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Order created successfully", responseEntity.getBody());

    }

    @Test
    @DisplayName("주문 수정")
    void testUpdateOrder() {
        // given
        Order existingOrder = new Order();
        existingOrder.setOrderNo(2500L);
        existingOrder.setOrderType(T);
        existingOrder.setOrderPrice(10000);
        existingOrder.setOrderRequest("많이 주세요");
        existingOrder.setRecipientPhone("010-3499-4444");
        existingOrder.setRecipientName("박승현");
        existingOrder.setRecipientZipcode("9999");
        existingOrder.setRecipientAddr("서울시 강남구");
        existingOrder.setRecipientDetailAddr("502-1호");

        // findOne 호출될 때 existingOrder를 리턴하도록 설정
        when(orderRepository.findByOrderNo(existingOrder.getOrderNo())).thenReturn(Optional.of(existingOrder));

        // 업데이트할 내용을 담은 새로운 Order 객체 생성
        Order updatedOrder = new Order();
        updatedOrder.setOrderNo(existingOrder.getOrderNo()); // 기존의 orderNo를 설정
        updatedOrder.setOrderType(T);
        updatedOrder.setOrderPrice(10000);
        updatedOrder.setOrderRequest("수정 요청");
        updatedOrder.setRecipientPhone("010-3499-4444");
        updatedOrder.setRecipientName("박승현");
        updatedOrder.setRecipientZipcode("9999");
        updatedOrder.setRecipientAddr("수정주소");
        updatedOrder.setRecipientDetailAddr("502-1호");

        // when
        // 서비스의 update 메소드 호출
        ResponseEntity<String> responseEntity = orderService.update(updatedOrder.getOrderNo());

        // then
        // 반환된 ResponseEntity의 상태 코드와 메시지를 확인
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Order updated successfully", responseEntity.getBody());
    }



        @Test
        @DisplayName("주문 삭제")
        void testDeleteOrder() {
            // Given
            Order mockOrder = new Order();
            mockOrder.setOrderType(T);
            mockOrder.setOrderPrice(10000);
            mockOrder.setOrderRequest("많이 주세요");
            mockOrder.setRecipientPhone("010-3499-4444");
            mockOrder.setRecipientName("박승현");
            mockOrder.setRecipientZipcode("9999");
            mockOrder.setRecipientAddr("서울시 강남구");
            mockOrder.setRecipientDetailAddr("502-1호");


            // findOne 메서드가 mockOrder를 반환하도록 설정
            when(orderRepository.findByOrderNo(mockOrder.getOrderNo())).thenReturn(Optional.of(mockOrder));

            // When
            orderService.deleteOrder(mockOrder.getOrderNo());

            // Then
            // mockNotice의 status가 N인지 확인
            assertEquals(OrderStatus.N, mockOrder.getOrderStatus());
    }

        @Test
        @DisplayName("가게 별 주문 조회 - 로그인 문제")
        void testFindOrdersByMemberAndStore_NoOrders() {
            // given
            Long memberNo = 123L;

            // 로그인한 사용자를 찾지 못하도록 설정
            when(memberService.findByMemberNo(memberNo)).thenReturn(Optional.empty());

            // when
            ResponseEntity<?> responseEntity = orderService.findOrdersByMemberAndStore(memberNo);

            // then
            assertEquals("로그인한 사용자가 아닙니다.", responseEntity.getBody());
        }

    @Test
    @DisplayName("가게 별 주문 조회 - 주문이 있는 경우")
    void testFindOrdersByMemberAndStore_WithOrders() {
        // given
        Long memberNo = 123L;

        Member fakeLoginUser = new Member();
        fakeLoginUser.setMemberNo(memberNo);

        when(memberService.findByMemberNo(memberNo)).thenReturn(Optional.of(fakeLoginUser));

        List<Order> fakeOrders = Arrays.asList(new Order(), new Order());

        // 주문이 있는 상황을 가정하고, 주문 리스트를 반환하도록 설정
        when(orderRepository.findOrdersByMemberAndStore(fakeLoginUser)).thenReturn(fakeOrders);

        // when
        ResponseEntity<?> responseEntity = orderService.findOrdersByMemberAndStore(memberNo);

        // then
        assertEquals(fakeOrders, responseEntity.getBody());
    }

    @Test
    @DisplayName("회원 별 주문 조회 - 로그인 문제")
    void testFindOrdersByMemberNo_NoOrders() {
        // given
        Long memberNo = 123L;

        // 로그인한 사용자를 찾지 못하도록 설정
        when(memberService.findByMemberNo(memberNo)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> responseEntity = orderService.findOrdersByMemberNo(memberNo);

        // then
        assertEquals("로그인한 사용자가 아닙니다.", responseEntity.getBody());
    }

    @Test
    @DisplayName("회원 별 주문 조회 - 주문이 있는 경우")
    void testFindOrdersByMemberNo_WithOrders() {
        // given
        Long memberNo = 123L;

        Member fakeLoginUser = new Member();
        fakeLoginUser.setMemberNo(memberNo);

        // 로그인한 사용자를 찾았다고 설정
        when(memberService.findByMemberNo(memberNo)).thenReturn(Optional.of(fakeLoginUser));

        // 가짜 주문 리스트를 생성
        List<Order> fakeOrders = Arrays.asList(new Order(), new Order());

        // 주문이 있는 상황을 가정하고, 주문 리스트를 반환하도록 설정
        when(orderRepository.findByOrderMemberMemberNo(fakeLoginUser)).thenReturn(fakeOrders);

        // when
        ResponseEntity<?> responseEntity = orderService.findOrdersByMemberNo(memberNo);

        // then
        assertEquals(fakeOrders, responseEntity.getBody());
    }


}