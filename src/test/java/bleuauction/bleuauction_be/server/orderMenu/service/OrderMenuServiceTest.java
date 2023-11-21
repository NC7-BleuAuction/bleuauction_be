package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository1;
import bleuauction.bleuauction_be.server.order.entity.Order;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;


@ExtendWith(MockitoExtension.class)
class OrderMenuServiceTest {

    @Mock
    private OrderMenuRepository1 orderMenuRepository;

    @InjectMocks
    private OrderMenuService1 orderMenuService;

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
        mockSavedOrderMenu.setOrderMenuNo(1L); // Assuming your save method sets this value
        when(orderMenuRepository.save(any(OrderMenuDTO.class)))
                .thenReturn(mockSavedOrderMenu);

        // When
        OrderMenuDTO savedOrderMenuDTO = orderMenuService.save(mockDTO);

        // Then
        // Verify that orderMenuRepository.save was called with the provided DTO
        verify(orderMenuRepository, times(1)).save(any(OrderMenuDTO.class));

        // Verify that the returned DTO has the expected values
        assertNotNull(savedOrderMenuDTO);
        assertEquals(mockSavedOrderMenu.getMenuNo(), savedOrderMenuDTO.getMenuNo());
    }

    @Test
    void testDeleteOrderMenu() {
        // Given
        Long orderMenuNo = 1L;
        OrderMenu mockOrderMenu = new OrderMenu();
        mockOrderMenu.setOrderMenuNo(orderMenuNo);

        // Mocking the findOne method
        when(orderMenuRepository.findByOrderMenuNo(orderMenuNo)).thenReturn(mockOrderMenu);

        // When
        orderMenuService.deleteOrderMenu(orderMenuNo);

        // Then
        // Verify that findOne method was called with the provided orderMenuNo
        verify(orderMenuRepository, times(1)).findByOrderMenuNo(orderMenuNo);
        assertEquals(mockOrderMenu.getOrderMenuStatus(), OrderMenuStatus.N);
    }

}