package bleuauction.bleuauction_be.server.orderMenu.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.service.OrderMenuService1;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ordermenu1")
public class OrderMenuController1 {

  private final OrderMenuService1 orderMenuService;
  private final CreateJwt createJwt;
  private final MemberService memberService;

  //등록
  @GetMapping("/new")
  public OrderMenu orderMenu() {
    OrderMenu orderMenu = new OrderMenu();
    return orderMenu;
  }


  @PostMapping("/new")
  public ResponseEntity<?> orderMenu(@RequestHeader("Authorization") String authorizationHeader, HttpSession session, OrderMenuDTO orderMenuDTO)  throws Exception {
    ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
    if (verificationResult != null) {
      return verificationResult;
    }

    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    Order order = (Order) session.getAttribute("order");

      return orderMenuService.addOrderMenuDTO(loginUser.get(), order, orderMenuDTO);
  }


  //주문 번호별 주문메뉴 조회
  @GetMapping("/{orderNo}")
  public List<OrderMenu> findOrderMenus(@PathVariable("orderNo") Long orderNo) {
      return orderMenuService.findOrderMenusByOrderNoAndStatusY(orderNo);
  }

  //주문별 주문 메뉴 조회
  @GetMapping("/order/{orderNo}")
  public List<OrderMenu> findOrderMenuDTOs(@PathVariable("orderNo") Long orderNo) {
      return orderMenuService.findOrderMenuDTOsByOrderNo(orderNo);
  }

  // 삭제
  @DeleteMapping("/{orderMenuNo}")
  public ResponseEntity<String> deleteOrderMenu(@PathVariable("orderMenuNo") Long orderMenuNo) {
    orderMenuService.deleteOrderMenu(orderMenuNo);
    return ResponseEntity.ok("OrderMenu deleted successfully");
  }


  //디테일(수정)
  @GetMapping("/detail/{orderMenuNo}")
  public ResponseEntity<OrderMenu> detailOM(@PathVariable("orderMenuNo") Long orderMenuNo) {
    OrderMenu OM = orderMenuService.findOne(orderMenuNo);
    return ResponseEntity.ok(OM);
  }

  @PutMapping("/update/{orderMenuNo}")
  public ResponseEntity<String> updateOM (OrderMenu orderMenu, @PathVariable("orderMenuNo") Long orderMenuNo) {
    orderMenuService.update(orderMenu);
    log.info("ordermenu/update");
    return ResponseEntity.ok("OrderMenu updated successfully");
  }
}