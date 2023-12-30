package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OrderModuleServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderModuleService orderModuleService;

}