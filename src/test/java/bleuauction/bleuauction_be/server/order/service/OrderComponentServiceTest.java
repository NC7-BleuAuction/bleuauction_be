package bleuauction.bleuauction_be.server.order.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OrderComponentServiceTest {

    @Mock
    private OrderModuleService orderModuleService;

    @InjectMocks private OrderComponentService orderComponentService;

}