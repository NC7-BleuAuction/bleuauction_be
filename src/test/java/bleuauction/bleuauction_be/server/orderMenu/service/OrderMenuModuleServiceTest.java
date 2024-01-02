package bleuauction.bleuauction_be.server.orderMenu.service;//package bleuauction.bleuauction_be.server.orderMenu.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderMenuModuleServiceTest {

    @Mock private OrderMenuRepository orderMenuRepository;

    @InjectMocks private OrderMenuModuleService orderMenuModuleService;


    @Test
    void testDeleteOrderMenu() {
        // Given
        Long orderMenuNo = 1L;
        OrderMenu mockOrderMenu = OrderMenu.builder().orderMenuStatus(OrderMenuStatus.Y).build();
        mockOrderMenu.setId(orderMenuNo);

        when(orderMenuRepository.findById(orderMenuNo)).thenReturn(Optional.of(mockOrderMenu));

        // When
        orderMenuModuleService.deleteOrderMenu(orderMenuNo);

        // Then
        verify(orderMenuRepository, times(1)).findById(orderMenuNo);
        assertEquals(mockOrderMenu.getOrderMenuStatus(), OrderMenuStatus.N);
    }
}
