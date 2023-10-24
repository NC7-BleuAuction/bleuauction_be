package bleuauction.bleuauction_be.server.orderMenu.service;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderMenuService {

  private final OrderMenuRepository orderMenuRepository;
  private final OrderRepository orderRepository;

  //등록
  public Long enroll(OrderMenu orderMenu) {
    orderMenuRepository.save(orderMenu);
    return orderMenu.getOrderMenuNo();
  }

  // DTO 등록
  public Long enroll(OrderMenuDTO orderMenuDTO) {
    OrderMenu orderMenu = copyDTOToEntity(orderMenuDTO);
    orderMenuRepository.save(orderMenu);
    return orderMenu.getOrderMenuNo();
  }
  // OrderMenuDTO를 OrderMenu로 복사하는 메서드
  public OrderMenu copyDTOToEntity(OrderMenuDTO dto) {
    OrderMenu orderMenu = new OrderMenu();
    orderMenu.setOrderMenuCount(dto.getOrderMenuCount());
    // 나머지 필드들도 복사해야 합니다.
    //orderMenu.setMenuNo(dto.getMenuNo());
    // orderMenu.setOrderNo(dto.getOrderNo());
    // orderMenu.setMemberNo(dto.getMemberNo());
    return orderMenu;
  }

  //주문메뉴 전체 조회
  @Transactional(readOnly = true)
  public List<OrderMenu> findOrderMenus() {
    return orderMenuRepository.findAll();
  }

  //주문메뉴 1건 조회
  @Transactional(readOnly = true)
  public OrderMenu findOne(Long orderMenuNo) {
    return orderMenuRepository.findOne(orderMenuNo);
  }

  //주문 번호 별 주문메뉴 조회
  @Transactional(readOnly = true)
  public List<OrderMenu> findOrderMenusByOrderNo(Long orderNo) {
    Order order = orderRepository.findOne(orderNo);
    if (order != null) {
      return order.getOrderMenus();
    } else {
      // 주어진 orderNo에 해당하는 주문을 찾지 못한 경우 또는 주문에 해당하는 메뉴가 없는 경우 처리
      return new ArrayList<>();
    }
  }

  //주문 메뉴 삭제
  public void deleteOrderMenu(Long orderMenuNo) {
    OrderMenu orderMenu = orderMenuRepository.findOne(orderMenuNo);
    orderMenu.delete();
  }

  //주문 메뉴 수정
  public OrderMenu update(OrderMenu orderMenu) {
    OrderMenu updateom = orderMenuRepository.findOne(orderMenu.getOrderMenuNo());

    updateom.setMemberNo(orderMenu.getMemberNo());
    updateom.setMenuNo(orderMenu.getMenuNo());
    //updateom.setOrderNo(orderMenu.getOrderNo());
    updateom.setOrderMenuCount(orderMenu.getOrderMenuCount());
    orderMenuRepository.save(orderMenu);

    return updateom;
  }
}