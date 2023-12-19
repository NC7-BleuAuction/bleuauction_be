package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.order.entity.Order;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderMenuServiceTest {

    @Mock
    private OrderMenuRepository orderMenuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMenuModuleService orderMenuModuleServiceM;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderMenuComponentService orderMenuComponentService;

    @InjectMocks
    private OrderMenuModuleService orderMenuModuleService;



    @Test
    void testSave() {
        // Given
        OrderMenuDTO mockDTO = new OrderMenuDTO();
        Menu mockMenu = new Menu();
        mockMenu.setMenuNo(100L);
        Order mockOrder = new Order();
        mockOrder.setOrderNo(100L);
        Member mockMember = new Member();
        mockMember.setMemberNo(100L);

        mockDTO.setOrderMenuCount(2);
        mockDTO.setMenuNo(mockMenu);
        mockDTO.setOrderNo(mockOrder);
        mockDTO.setMemberNo(mockMember);

        // When
        orderMenuModuleService.saveDTO(mockDTO);

        // Then
        verify(orderMenuRepository, times(1)).save(any(OrderMenuDTO.class));

        assertNotNull(mockDTO);
        assertEquals(mockDTO.getOrderMenuCount(),2);
    }

    @Test
    void testDeleteOrderMenu() {
        // Given
        Long orderMenuNo = 1L;
        OrderMenu mockOrderMenu = new OrderMenu();
        mockOrderMenu.setOrderMenuNo(orderMenuNo);

        when(orderMenuRepository.findByOrderMenuNo(orderMenuNo)).thenReturn(mockOrderMenu);

        // When
        orderMenuModuleService.deleteOrderMenu(orderMenuNo);

        // Then
        verify(orderMenuRepository, times(1)).findByOrderMenuNo(orderMenuNo);
        assertEquals(mockOrderMenu.getOrderMenuStatus(), OrderMenuStatus.N);
    }


    @Test
    void testFindOrderMenuDTOsByOrderNo() {
        // Given
        Long orderNo = 123L;

        Order mockOrder = new Order();
        mockOrder.setOrderNo(orderNo);

        OrderMenu mockOrderMenu1 = new OrderMenu();
        mockOrderMenu1.setOrderMenuStatus(OrderMenuStatus.Y);

        OrderMenu mockOrderMenu2 = new OrderMenu();
        mockOrderMenu2.setOrderMenuStatus(OrderMenuStatus.N); // 이 메뉴는 결과에 포함되지 않아야 함

        when(orderRepository.findByOrderNo(orderNo)).thenReturn(Optional.of(mockOrder));

        // Mock Order에 OrderMenu 추가
        mockOrder.setOrderMenus(List.of(mockOrderMenu1, mockOrderMenu2));

        // When
        List<OrderMenu> result = orderMenuComponentService.findOrderMenuDTOsByOrderNo(orderNo);

        // Then
        verify(orderRepository, times(1)).findByOrderNo(orderNo);
        assertEquals(OrderMenuStatus.Y, result.get(0).getOrderMenuStatus());
    }


    @Test
    void testFindOrderMenusByOrderNoAndStatusY() {
        // Given
        Long orderNo = 1L;

        // Create an order
        Order mockOrder = new Order();
        mockOrder.setOrderNo(orderNo);

        // Create order menus with different statuses
        OrderMenu orderMenu1 = new OrderMenu();
        orderMenu1.setOrderMenuStatus(OrderMenuStatus.Y);
        orderMenu1.setOrderNo(mockOrder);

        OrderMenu orderMenu2 = new OrderMenu();
        orderMenu2.setOrderMenuStatus(OrderMenuStatus.N);
        orderMenu2.setOrderNo(mockOrder);

        OrderMenu orderMenu3 = new OrderMenu();
        orderMenu3.setOrderMenuStatus(OrderMenuStatus.Y);
        orderMenu3.setOrderNo(mockOrder);

        mockOrder.setOrderMenus(List.of(orderMenu1, orderMenu2, orderMenu3));
        when(orderService.findOne(orderNo)).thenReturn(Optional.of(mockOrder));

        //when
        List<OrderMenu> result = orderMenuComponentService.findOrderMenusByOrderNoAndStatusY(orderNo);


        // Then
        assertEquals(2, result.size());
        assertEquals(OrderMenuStatus.Y, result.get(0).getOrderMenuStatus());
        assertEquals(OrderMenuStatus.Y, result.get(1).getOrderMenuStatus());

    }

    @Test
    @DisplayName("수정하기")
    void testUpdateOrderMenu1()  {
        // Given
        long orderMenuNo = 1L;

        OrderMenu existingOrderMenu = new OrderMenu();
        Member mockMember = new Member();
        Menu mockMenu = new Menu();
        Menu mockMenu2 = new Menu();
        existingOrderMenu.setOrderMenuNo(orderMenuNo);
        existingOrderMenu.setMemberNo(mockMember);  // You might need to create a mock Member
        existingOrderMenu.setMenuNo(mockMenu);      // You might need to create a mock Menu
        existingOrderMenu.setOrderMenuCount(3);
        orderMenuModuleService.save(existingOrderMenu);


        OrderMenuDTO updatingOrderMenu = new OrderMenuDTO();
        updatingOrderMenu.setOrderMenuNo(existingOrderMenu.getOrderMenuNo());
        updatingOrderMenu.setMemberNo(mockMember);
        updatingOrderMenu.setMenuNo(mockMenu2);
        updatingOrderMenu.setOrderMenuCount(4);

        when(orderMenuModuleServiceM.findOne(orderMenuNo))
                .thenReturn(existingOrderMenu);


        // When
        orderMenuComponentService.update(orderMenuNo,updatingOrderMenu);

        // Then
        assertEquals(updatingOrderMenu.getMenuNo(), existingOrderMenu.getMenuNo());
        assertEquals(updatingOrderMenu.getOrderMenuCount(), existingOrderMenu.getOrderMenuCount());

    }


}