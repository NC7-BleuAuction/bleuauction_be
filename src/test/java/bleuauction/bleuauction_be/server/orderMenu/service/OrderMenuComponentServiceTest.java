package bleuauction.bleuauction_be.server.orderMenu.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OrderMenuComponentServiceTest {

    @Mock private OrderMenuModuleService orderMenuModuleService;
    @InjectMocks private OrderMenuComponentService orderMenuComponentService;

}