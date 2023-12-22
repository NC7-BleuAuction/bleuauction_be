package bleuauction.bleuauction_be.server.orderMenu.service;


import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.service.MenuModuleService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;

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

    private final OrderMenuRepository orderMenuRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final MenuModuleService menuModuleService;
    private final OrderMenuModuleService orderMenuModuleService;

    public ResponseEntity<String> addOrderMenuDTO(Member member, Order order, OrderMenuDTO orderMenuDTO) {
        Menu selectedMenu = menuModuleService.findOne(orderMenuDTO.getMenu().getId());
        OrderMenu orderMenu = OrderMenu.builder()
                .member(member)
                .order(order)
                .orderMenuCount(orderMenuDTO.getOrderMenuCount())
                .menu(selectedMenu)
                .build();

        orderMenuRepository.save(orderMenu);
        log.info("ordermenu/postnew");
        return ResponseEntity.ok("OrderMenu created successfully");
    }

    // 주문 번호 별 주문메뉴 조회
    @Transactional(readOnly = true)
    public List<OrderMenu> findOrderMenusByOrderNoAndStatusY(Long orderNo) {

        Optional<Order> orderOptional = orderService.findOne(orderNo);

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
