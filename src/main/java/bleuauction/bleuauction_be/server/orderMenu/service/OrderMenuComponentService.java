package bleuauction.bleuauction_be.server.orderMenu.service;


import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.service.MenuModuleService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderModuleService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class OrderMenuComponentService {
    private final OrderRepository orderRepository;
    private final OrderModuleService orderModuleService;
    private final OrderMenuModuleService orderMenuModuleService;

    public ResponseEntity<String> addOrderMenu(Member member, Order order, OrderMenu orderMenu) {
        orderMenu.setMember(member);
        orderMenu.setOrder(order);
        orderMenuModuleService.save(orderMenu);
        log.info("ordermenu/postnew");
        return ResponseEntity.ok("OrderMenu created successfully");
    }

    // 주문 번호 별 주문메뉴 조회
    @Transactional(readOnly = true)
    public List<OrderMenu> findOrderMenusByOrderNoAndStatusY(Long orderNo) {

        Optional<Order> orderOptional = orderModuleService.findOne(orderNo);

        if (orderOptional.isEmpty()) {
            return new ArrayList<>();
        }

        Order order = orderOptional.get();
        List<OrderMenu> orderMenus = order.getOrderMenus();
        List<OrderMenu> result = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus) {
            if (orderMenu.getOrderMenuStatus().equals(OrderMenuStatus.Y)) {
                result.add(orderMenu);
            }
        }
        return result;
    }

    // 주문 메뉴 수정
    public OrderMenu update(Long orderMenuNo, OrderMenuDTO request) {
        OrderMenu existingOrderMenu = orderMenuModuleService.findOne(orderMenuNo);

        if (existingOrderMenu == null) {
            throw new IllegalArgumentException("주문 메뉴가 존재하지 않습니다.");
        }

        existingOrderMenu.setMember(request.getMember());
        existingOrderMenu.setMenu(request.getMenu());
        existingOrderMenu.setOrderMenuCount(request.getOrderMenuCount());

        return orderMenuModuleService.save(existingOrderMenu);
    }

    @Transactional(readOnly = true)
    public List<OrderMenu> findOrderMenuDTOsByOrderNo(Long orderNo) {
        Optional<Order> order = orderRepository.findById(orderNo);

        if (order.isEmpty()) {
            return new ArrayList<>();
        }
        List<OrderMenu> orderMenus = order.get().getOrderMenus();
        List<OrderMenu> result = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus) {
            if (orderMenu.getOrderMenuStatus().equals(OrderMenuStatus.Y)) {
                result.add(orderMenu);
            }
        }
        return result;
    }
}
