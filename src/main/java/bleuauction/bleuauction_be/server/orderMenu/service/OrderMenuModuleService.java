package bleuauction.bleuauction_be.server.orderMenu.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.exception.OrderMenuNotFoundException;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class OrderMenuModuleService {
    private final OrderMenuRepository orderMenuRepository;


    public OrderMenu save(OrderMenu orderMenu) {
        return orderMenuRepository.save(orderMenu);
    }

    // 주문 메뉴 삭제
    public void deleteOrderMenu(Long orderMenuNo) {
        Optional<OrderMenu> orderMenu = orderMenuRepository.findById(orderMenuNo);
        if (orderMenu.isPresent()) {
            OrderMenu orderMenuGet = orderMenu.get();
            orderMenuGet.delete();
            orderMenuRepository.save(orderMenuGet);
        }
    }

    // 주문 메뉴 1건 조회
    public OrderMenu findOne(Long orderMenuNo) {
        return orderMenuRepository
                .findById(orderMenuNo)
                .orElseThrow(() -> OrderMenuNotFoundException.EXCEPTION);
    }
}
