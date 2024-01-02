package bleuauction.bleuauction_be.server.orderMenu.service;//package bleuauction.bleuauction_be.server.orderMenu.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.service.MenuModuleService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderModuleService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class OrderMenuComponentServiceTest {


    @Mock private OrderRepository orderRepository;
    @Mock private OrderModuleService orderModuleService;
    @Mock private MenuModuleService menuModuleService;
    @Mock private OrderMenuModuleService orderMenuModuleService;

    @InjectMocks private OrderMenuComponentService orderMenuComponentService;

    @Test
    void enroll() {
        Member mockMember = Member.builder().build();
        Order mockOrder = Order.builder().build();
        Menu mockMenu = Menu.builder().build();
        mockMenu.setId(1L);


        OrderMenu mockorderMenu = OrderMenu.builder()
                .member(mockMember)
                .order(mockOrder)
                .menu(mockMenu)
                .build();


        when(orderMenuModuleService.save(mockorderMenu)).thenReturn(mockorderMenu);


        ResponseEntity<String> result = orderMenuComponentService.addOrderMenu(mockMember,mockOrder,mockorderMenu);

        assertEquals(result.getBody(),"OrderMenu created successfully");
    }
    @Test
    void testFindOrderMenuDTOsByOrderNo() {
        // Given
        Long orderNo = 123L;

        Order mockOrder = Order.builder().build();
        mockOrder.setId(orderNo);

        OrderMenu mockOrderMenu1 = OrderMenu.builder().build();
        mockOrderMenu1.setOrderMenuStatus(OrderMenuStatus.Y);

        OrderMenu mockOrderMenu2 = OrderMenu.builder().build();
        mockOrderMenu2.setOrderMenuStatus(OrderMenuStatus.N); // 이 메뉴는 결과에 포함되지 않아야 함

        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(mockOrder));

        // Mock Order에 OrderMenu 추가
        mockOrder.setOrderMenus(List.of(mockOrderMenu1, mockOrderMenu2));

        // When
        List<OrderMenu> result = orderMenuComponentService.findOrderMenuDTOsByOrderNo(orderNo);

        // Then
        verify(orderRepository, times(1)).findById(orderNo);
        assertEquals(OrderMenuStatus.Y, result.get(0).getOrderMenuStatus());
    }

    @Test
    @DisplayName("수정하기")
    void testUpdateOrderMenu1() {
        // Given
        long orderMenuNo = 1L;

        Member mockMember = Member.builder().build();
        Menu mockMenu = Menu.builder().build();
        Menu mockMenu2 = Menu.builder().build();

        OrderMenu existingOrderMenu =
                OrderMenu.builder().member(mockMember).menu(mockMenu).orderMenuCount(3).build();
        existingOrderMenu.setId(orderMenuNo);

        orderMenuModuleService.save(existingOrderMenu);

        OrderMenuDTO updatingOrderMenu = new OrderMenuDTO();
        updatingOrderMenu.setId(existingOrderMenu.getId());
        updatingOrderMenu.setMember(mockMember);
        updatingOrderMenu.setMenu(mockMenu2);
        updatingOrderMenu.setOrderMenuCount(4);

        when(orderMenuModuleService.findOne(orderMenuNo)).thenReturn(existingOrderMenu);

        // When
        orderMenuComponentService.update(orderMenuNo, updatingOrderMenu);

        // Then
        assertEquals(updatingOrderMenu.getMenu(), existingOrderMenu.getMenu());
        assertEquals(updatingOrderMenu.getOrderMenuCount(), existingOrderMenu.getOrderMenuCount());
    }


        @Test
        void testFindOrderMenusByOrderNoAndStatusY() {
        // Given
        Long orderNo = 1L;

        // Create an order
        Optional<Order> mockOrder = Optional.ofNullable(Order.builder().build());
        mockOrder.get().setId(orderNo);

        // Create order menus with different statuses
        OrderMenu orderMenu1 = OrderMenu.builder().build();
        orderMenu1.setOrderMenuStatus(OrderMenuStatus.Y);
        orderMenu1.setOrder(mockOrder.get());

        OrderMenu orderMenu2 = OrderMenu.builder().build();
        orderMenu2.setOrderMenuStatus(OrderMenuStatus.N);
        orderMenu2.setOrder(mockOrder.get());

        OrderMenu orderMenu3 = OrderMenu.builder().build();
        orderMenu3.setOrderMenuStatus(OrderMenuStatus.Y);
        orderMenu3.setOrder(mockOrder.get());

        when(orderModuleService.findOne(orderNo)).thenReturn(mockOrder);

            // when
        List<OrderMenu> result =
                orderMenuComponentService.findOrderMenusByOrderNoAndStatusY(orderNo);

        // Then
        assertEquals(2, result.size());
        assertEquals(OrderMenuStatus.Y, result.get(0).getOrderMenuStatus());
        assertEquals(OrderMenuStatus.Y, result.get(1).getOrderMenuStatus());
    }
}
