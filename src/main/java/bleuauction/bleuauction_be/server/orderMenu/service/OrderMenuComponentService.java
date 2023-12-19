package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.service.MenuModuleService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class OrderMenuComponentService {

    private final OrderMenuRepository orderMenuRepository;
    private final OrderRepository orderRepository;
    private final MenuModuleService menuModuleService;
    private final OrderMenuModuleService orderMenuModuleService;

    public ResponseEntity<String> addOrderMenuDTO(Member member, Order order, OrderMenuDTO orderMenuDTO) {
        Optional<Menu> selectedMenu = Optional.ofNullable(menuModuleService.findOne(orderMenuDTO.getMenuNo()));

        if(selectedMenu.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Selected menu not found");
        }
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setMemberNo(member);
            orderMenu.setOrderNo(order);
            orderMenu.setOrderMenuCount(orderMenuDTO.getOrderMenuCount());
            orderMenu.setMenuNo(selectedMenu.get());

            orderMenuRepository.save(orderMenu);
            log.info("ordermenu/postnew");
            return ResponseEntity.ok("OrderMenu created successfully");
    }

    //주문 번호 별 주문메뉴 조회
    @Transactional(readOnly = true)
    public List<OrderMenu> findOrderMenusByOrderNoAndStatusY(Long orderNo) {
        Optional<Order> order = orderRepository.findById(orderNo);

        if(order == null) {
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

    //주문 메뉴 수정
    public OrderMenu update(OrderMenu orderMenu) {
        OrderMenu updateom = orderMenuRepository.findByOrderMenuNo(orderMenu.getOrderMenuNo());

        updateom.setMemberNo(orderMenu.getMemberNo());
        updateom.setMenuNo(orderMenu.getMenuNo());
        updateom.setOrderMenuCount(orderMenu.getOrderMenuCount());

        return orderMenuModuleService.save(updateom);
    }


    @Transactional(readOnly = true)
    public List<OrderMenu> findOrderMenuDTOsByOrderNo(Long orderNo) {
        Optional<Order> order = orderRepository.findByOrderNo(orderNo);

        if(order == null) {
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
