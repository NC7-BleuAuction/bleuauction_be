package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository1;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderMenuService1 {

  private final OrderMenuRepository1 orderMenuRepository;
  private final OrderRepository1 orderRepository;
  private final MenuRepository menuRepository;

  //등록

  public ResponseEntity<?> addOrderMenuDTO(Member memberNo, Order order, OrderMenuDTO orderMenuDTO) throws Exception {
    Menu selectedMenu = menuRepository.findOne(orderMenuDTO.getMenuNo());

    if (selectedMenu != null) {
      OrderMenu orderMenu = new OrderMenu();
      orderMenu.setMemberNo(memberNo);
      orderMenu.setOrderNo(order);
      orderMenu.setOrderMenuCount(orderMenuDTO.getOrderMenuCount());
      orderMenu.setMenuNo(selectedMenu);

      orderMenuRepository.save(orderMenu);
      log.info("ordermenu/postnew");
      return ResponseEntity.status(HttpStatus.CREATED).body("OrderMenu created successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Selected menu not found");
    }
  }

  // DTO 등록
  public OrderMenuDTO save(OrderMenuDTO orderMenuDTO) {
    return orderMenuRepository.save(orderMenuDTO);
  }

  //주문메뉴 전체 조회
  @Transactional(readOnly = true)
  public List<OrderMenu> findOrderMenus() {
    return orderMenuRepository.findAll();
  }

  //주문메뉴 1건 조회
  @Transactional(readOnly = true)
  public OrderMenu findOne(Long orderMenuNo) {

    return orderMenuRepository.findByOrderMenuNo(orderMenuNo);
  }

  //주문 번호 별 주문메뉴 조회
//  @Transactional(readOnly = true)
//  public List<OrderMenu> findOrderMenusByOrderNo(Long orderNo) {
//    Order order = orderRepository.findOne(orderNo);
//    if (order != null) {
//      return order.getOrderMenus();
//    } else {
//      // 주어진 orderNo에 해당하는 주문을 찾지 못한 경우 또는 주문에 해당하는 메뉴가 없는 경우 처리
//      return new ArrayList<>();
//    }
//  }







  @Transactional(readOnly = true)
  public List<OrderMenu> findOrderMenusByOrderNoAndStatusY(Long orderNo) {
    Optional<Order> order = orderRepository.findByOrderNo(orderNo);
      if (order != null) {
        List<OrderMenu> orderMenus = order.get().getOrderMenus();
        List<OrderMenu> result = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus) {
          if (orderMenu.getOrderMenuStatus().equals(OrderMenuStatus.Y)) {
            result.add(orderMenu);
          }
        }

        return result;
      } else {
        // 주어진 orderNo에 해당하는 주문을 찾지 못한 경우 또는 주문에 해당하는 메뉴가 없는 경우 처리
        return new ArrayList<>();
      }
  }

  //주문 메뉴 삭제

  public void deleteOrderMenu(Long orderMenuNo) {
    OrderMenu orderMenu = findOne(orderMenuNo);
    if (orderMenu != null) {
      orderMenu.delete();
      orderMenuRepository.save(orderMenu);
    }
  }

  //주문 메뉴 수정
  public OrderMenu update(OrderMenu orderMenu) {
    OrderMenu updateom = orderMenuRepository.findByOrderMenuNo(orderMenu.getOrderMenuNo());

    updateom.setMemberNo(orderMenu.getMemberNo());
    updateom.setMenuNo(orderMenu.getMenuNo());
    updateom.setOrderMenuCount(orderMenu.getOrderMenuCount());
    orderMenuRepository.save(orderMenu);

    return updateom;
  }


  @Transactional(readOnly = true)
  public List<OrderMenu> findOrderMenuDTOsByOrderNo(Long orderNo) {
    Optional<Order> order = orderRepository.findByOrderNo(orderNo);
    if (order != null) {
      List<OrderMenu> orderMenus = order.get().getOrderMenus();
      List<OrderMenu> result = new ArrayList<>();

      for (OrderMenu orderMenu : orderMenus) {
        if (orderMenu.getOrderMenuStatus().equals(OrderMenuStatus.Y)) {
          result.add(orderMenu);
        }
      }

      return result;
    } else {
      // 주어진 orderNo에 해당하는 주문을 찾지 못한 경우 또는 주문에 해당하는 메뉴가 없는 경우 처리
      return new ArrayList<>();
    }
  }


}