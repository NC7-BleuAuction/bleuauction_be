package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.order.entity.Order;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.order.entity.OrderType;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;

import bleuauction.bleuauction_be.server.store.entity.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private MenuRepository menuRepository;

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
        mockDTO.setMenuNo(mockMenu.getMenuNo());
        mockDTO.setOrderNo(mockOrder.getOrderNo());
        mockDTO.setMemberNo(mockMember.getMemberNo());

        OrderMenuDTO mockSavedOrderMenu = new OrderMenuDTO();
        mockSavedOrderMenu.setOrderMenuNo(1L);
        when(orderMenuRepository.save(any(OrderMenuDTO.class)))
                .thenReturn(mockSavedOrderMenu);

        // When
        OrderMenuDTO savedOrderMenuDTO = orderMenuModuleService.save(mockDTO);

        // Then
        verify(orderMenuRepository, times(1)).save(any(OrderMenuDTO.class));

        assertNotNull(savedOrderMenuDTO);
        assertEquals(mockSavedOrderMenu.getMenuNo(), savedOrderMenuDTO.getMenuNo());
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
    void testUpdateOrderMenu() {
        // Given
        OrderMenu mockOrderMenuToUpdate = new OrderMenu();
        Member mockMember = new Member();
        Menu mockMenu = new Menu();
        Menu mockMenu2 = new Menu();
        mockOrderMenuToUpdate.setOrderMenuNo(1L);
        mockOrderMenuToUpdate.setMemberNo(mockMember);
        mockOrderMenuToUpdate.setMenuNo(mockMenu);
        mockOrderMenuToUpdate.setOrderMenuCount(2);

        OrderMenu mockExistingOrderMenu = new OrderMenu();
        mockExistingOrderMenu.setOrderMenuNo(1L);
        mockExistingOrderMenu.setMemberNo(mockMember);
        mockExistingOrderMenu.setMenuNo(mockMenu2);
        mockExistingOrderMenu.setOrderMenuCount(3);

        when(orderMenuModuleService.findByOrderMenuNo(mockOrderMenuToUpdate.getOrderMenuNo()))
                .thenReturn(mockExistingOrderMenu);

        // When
        OrderMenu updatedOrderMenu = orderMenuComponentService.update(mockOrderMenuToUpdate);

        // Then
        verify(orderMenuModuleService, times(1)).findByOrderMenuNo(mockOrderMenuToUpdate.getOrderMenuNo());

        assertEquals(mockOrderMenuToUpdate.getMemberNo(), mockExistingOrderMenu.getMemberNo());
        assertEquals(mockOrderMenuToUpdate.getMenuNo(), mockExistingOrderMenu.getMenuNo());
        assertEquals(mockOrderMenuToUpdate.getOrderMenuCount(), mockExistingOrderMenu.getOrderMenuCount());


        assertEquals(mockExistingOrderMenu, updatedOrderMenu);
    }

    @Test
    void testAddOrderMenuDTO_WhenMenuExists() throws Exception {
        // 주어진 상황(Given)
        Member mockMember = new Member();
        mockMember.setMemberEmail("psh@test.com");
        mockMember.setMemberPwd("pwd");
        mockMember.setMemberName("박승현");
        mockMember.setMemberZipcode("12345");
        mockMember.setMemberAddr("강남구");
        mockMember.setMemberDetailAddr("자곡로");
        mockMember.setMemberPhone("123-456-7890");
        mockMember.setMemberBank("신한");
        mockMember.setMemberAccount("110-467");
        mockMember.setMemberCategory(MemberCategory.S);

        Order mockOrder = new Order();
        mockOrder.setOrderType(OrderType.Q);
        mockOrder.setOrderPrice(10000);
        mockOrder.setOrderRequest("Special request for the order");
        mockOrder.setRecipientPhone("123-456-7890");
        mockOrder.setRecipientName("Recipient Name");
        mockOrder.setRecipientZipcode("54321");
        mockOrder.setRecipientAddr("Recipient Address");
        mockOrder.setRecipientDetailAddr("Recipient Detail Address");


        Menu mockSelectedMenu = new Menu();
        mockSelectedMenu.setMenuName("Sample Menu");
        mockSelectedMenu.setMenuSize(MenuSize.L);
        mockSelectedMenu.setMenuPrice(15000);
        mockSelectedMenu.setMenuContent("Sample Menu Content");

        Store mockStore = new Store();
        mockStore.setStoreNo(1L);
        mockSelectedMenu.setStoreNo(mockStore);


        OrderMenuDTO mockOrderMenuDTO = new OrderMenuDTO();
        mockOrderMenuDTO.setMenuNo(mockSelectedMenu.getMenuNo());
        mockOrderMenuDTO.setOrderMenuCount(2);
        mockOrderMenuDTO.setOrderNo(mockOrder.getOrderNo());
        mockOrderMenuDTO.setMemberNo(mockMember.getMemberNo());


        when(menuRepository.findMenusByMenuNo(mockOrderMenuDTO.getMenuNo()))
                .thenReturn(mockSelectedMenu);

        // When
        ResponseEntity<?> responseEntity = orderMenuComponentService.addOrderMenuDTO(mockMember, mockOrder, mockOrderMenuDTO);

        // Then
        // findMenusByMenuNo 메서드가 주어진 메뉴 번호로 호출되었는지 확인
        verify(menuRepository, times(1)).findMenusByMenuNo(mockOrderMenuDTO.getMenuNo());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("OrderMenu created successfully", responseEntity.getBody());
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
        List<OrderMenu> result = orderMenuComponentService.findOrderMenusByOrderNoAndStatusY(orderNo);

        // Then
        verify(orderRepository, times(1)).findByOrderNo(orderNo);
        assertEquals(OrderMenuStatus.Y, result.get(0).getOrderMenuStatus());
    }

}