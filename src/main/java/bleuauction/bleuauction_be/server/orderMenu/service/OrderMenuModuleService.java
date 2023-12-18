package bleuauction.bleuauction_be.server.orderMenu.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class OrderMenuModuleService {
    private final OrderMenuRepository orderMenuRepository;

    // DTO 등록
    public OrderMenuDTO save(OrderMenuDTO orderMenuDTO) {
        return orderMenuRepository.save(orderMenuDTO);
    }

    //주문 메뉴 삭제
    public void deleteOrderMenu(Long orderMenuNo) {
        OrderMenu orderMenu = orderMenuRepository.findByOrderMenuNo(orderMenuNo);
        if (orderMenu != null) {
            orderMenu.delete();
            orderMenuRepository.save(orderMenu);
        }
    }
}
