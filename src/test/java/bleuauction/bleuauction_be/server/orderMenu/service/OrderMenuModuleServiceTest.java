package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderMenuModuleServiceTest {

    @Mock
    private OrderMenuRepository orderMenuRepository;
    @InjectMocks
    private OrderMenuModuleService orderMenuModuleService;

}