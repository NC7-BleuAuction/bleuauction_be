package bleuauction.bleuauction_be.server.orderMenu.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderMenuController {

  private final OrderMenuService orderMenuService;
  private final OrderService orderService;
  private final OrderMenuRepository orderMenuRepository;

  //등록
  @GetMapping("/api/ordermenu/new")
  public OrderMenu orderMenu() {
//    log.info("orderList: " + orderList );
    OrderMenu orderMenu = new OrderMenu();
    return orderMenu;
  }

  //프론트 페이지 만들어지면 확인 가능
  @PostMapping("/api/ordermenu/new")
  @Transactional
  public ResponseEntity<String> orderMenu(OrderMenu orderMenu, HttpSession session) {
    System.out.println(orderMenu);
    //로그인유저의 멤버번호
    Member member = (Member) session.getAttribute("loginUser");
    orderMenu.setMemberNo(member);
    //오더 세션의 오더
    Order order = (Order) session.getAttribute("order");
    orderMenu.setOrderNo(order);
    //메뉴는 뭘로 하지?
    //orderMenu.setMenuNo(Menu); 프론트에서 메뉴 체크하면 메뉴 넘어가게 만들기.
    //axios.post('/api/ordermenu/new', {
    //    menuNo: 2, // 선택한 메뉴의 ID
    //    orderMenuCount: 5
    //})
    //.then(response => {
    //    console.log(response.data);
    //})
    //.catch(error => {
    //    console.error(error);
    //});

    orderMenuService.enroll(orderMenu);
    log.info("ordermenu/postnew");
    return ResponseEntity.status(HttpStatus.CREATED).body("OrderMenu created successfully");
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

  @PostMapping("/api/ordermenu/update/{orderMenuNo}")
  public ResponseEntity<String> updateOM (OrderMenu orderMenu, @PathVariable("orderMenuNo") Long orderMenuNo) {
    orderMenuService.update(orderMenu);
    log.info("ordermenu/update");
    return ResponseEntity.ok("OrderMenu updated successfully");
  }
}
