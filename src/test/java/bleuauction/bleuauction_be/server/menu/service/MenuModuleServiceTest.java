package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MenuModuleServiceTest {

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    MenuModuleService menuModuleService;

}