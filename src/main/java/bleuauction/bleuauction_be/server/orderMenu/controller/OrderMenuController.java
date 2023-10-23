package bleuauction.bleuauction_be.server.orderMenu.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import bleuauction.bleuauction_be.server.orderMenu.service.OrderMenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderMenuController {

  private final OrderMenuService orderMenuService;
  private final OrderService orderService;
  private final OrderMenuRepository orderMenuRepository;
  private final MenuRepository menuRepository;

  //등록
  @GetMapping("/api/ordermenu/new")
  public OrderMenu orderMenu() {
    OrderMenu orderMenu = new OrderMenu();
    return orderMenu;
  }

//  @GetMapping("/api/ordermenu/new")
//  public OrderMenuDTO createOrderMenuDTO(
//          @RequestParam Long orderMenuNo,
//          @RequestParam int orderMenuCount,
//          @RequestParam Timestamp regDatetime,
//          @RequestParam Timestamp mdfDatetime,
//          @RequestParam OrderMenuStatus orderMenuStatus,
//          @RequestParam Long menuNo,
//          @RequestParam Long orderNo,
//          @RequestParam Long memberNo
//  ) {
//    OrderMenuDTO orderMenuDTO = new OrderMenuDTO();
//
//    orderMenuDTO.setOrderMenuNo(orderMenuNo);
//    orderMenuDTO.setOrderMenuCount(orderMenuCount);
//    orderMenuDTO.setRegDatetime(regDatetime);
//    orderMenuDTO.setMdfDatetime(mdfDatetime);
//    orderMenuDTO.setOrderMenuStatus(orderMenuStatus);
//    orderMenuDTO.setMenuNo(menuNo);
//    orderMenuDTO.setOrderNo(orderNo);
//    orderMenuDTO.setMemberNo(memberNo);
//
//    return orderMenuDTO;
//  }


  //프론트 페이지 만들어지면 확인 가능
  @PostMapping("/api/ordermenu/new")
  @Transactional
  public ResponseEntity<String> orderMenu(HttpSession session, OrderMenuDTO orderMenuDTO) {
    // 로그인 유저의 멤버 번호
    Member member = (Member) session.getAttribute("loginUser");
    // 오더 세션의 오더
    Order order = (Order) session.getAttribute("order");


    // 이 부분에서 selectedMenuId를 사용하여 Menu 엔티티를 조회하고 orderMenu 엔티티에 할당합니다.
    Menu selectedMenu = menuRepository.findOne(orderMenuDTO.getMenuNo());

    if (selectedMenu != null) {
      OrderMenu orderMenu = new OrderMenu();
      orderMenu.setMemberNo(member);
      orderMenu.setOrderNo(order);
      orderMenu.setOrderMenuCount(orderMenuDTO.getOrderMenuCount());
      orderMenu.setMenuNo(selectedMenu);

      orderMenuService.enroll(orderMenu);
      log.info("ordermenu/postnew");
      return ResponseEntity.status(HttpStatus.CREATED).body("OrderMenu created successfully");
    } else {
      // 선택한 메뉴가 없을 때 처리
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Selected menu not found");
    }
  }

  //주문 번호별 주문메뉴 조회
  @GetMapping("/api/ordermenu/{orderNo}")
  public List<OrderMenu> findOM(HttpSession session, @PathVariable("orderNo") Long orderNo) throws Exception {
    //Order order = (Order) session.getAttribute("order");

    Order order = orderService.findOne(orderNo);

    try {
      List<OrderMenu> orderMenus = orderMenuService.findOrderMenusByOrderNo(order.getOrderNo());
      return orderMenus;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
  @GetMapping("/api/ordermenu/order/{orderNo}")
  public List<OrderMenuDTO> findorderOM(HttpSession session, @PathVariable("orderNo") Long orderNo) throws Exception {
    Order order = orderService.findOne(orderNo);

    try {
      List<OrderMenu> orderMenus = orderMenuService.findOrderMenusByOrderNo(order.getOrderNo());

      // OrderMenu 엔터티를 OrderMenuDTO로 매핑하여 반환
      List<OrderMenuDTO> orderMenuDTOs = orderMenus.stream()
              .map(orderMenu -> {
                OrderMenuDTO dto = new OrderMenuDTO();
                dto.setOrderMenuNo(orderMenu.getOrderMenuNo());
                dto.setOrderMenuCount(orderMenu.getOrderMenuCount());
                dto.setRegDatetime(orderMenu.getRegDatetime());
                dto.setMdfDatetime(orderMenu.getMdfDatetime());
                dto.setOrderMenuStatus(orderMenu.getOrderMenuStatus());
                dto.setMenuNo(orderMenu.getMenuNo().getMenuNo()); // 메뉴 번호
                dto.setOrderNo(orderMenu.getOrderNo().getOrderNo()); // 주문 번호
                return dto;
              })
              .collect(Collectors.toList());

      return orderMenuDTOs;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  // 삭제
  @PostMapping("api/ordermenu/delete/{orderMenuNo}")
  public ResponseEntity<String> deleteOM(@PathVariable("orderMenuNo") Long orderMenuNo) {
    OrderMenu OM = orderMenuService.findOne(orderMenuNo);
    if (OM != null) {
      OM.delete();
      orderMenuService.enroll(OM);
    }
    return ResponseEntity.ok("OrderMenu deleted successfully");
  }

  //디테일(수정)
  @GetMapping("/api/ordermenu/detail/{orderMenuNo}")
  public ResponseEntity<OrderMenu> detailOM(@PathVariable("orderMenuNo") Long orderMenuNo) {
    OrderMenu OM = orderMenuService.findOne(orderMenuNo);
    return ResponseEntity.ok(OM);
  }

//  @PostMapping("/api/ordermenu/update/{orderMenuNo}")
//  public ResponseEntity<String> updateOM (OrderMenu orderMenu, @PathVariable("orderMenuNo") Long orderMenuNo) {
//    orderMenuService.update(orderMenu);
//    log.info("ordermenu/update");
//    return ResponseEntity.ok("OrderMenu updated successfully");
//  }
}