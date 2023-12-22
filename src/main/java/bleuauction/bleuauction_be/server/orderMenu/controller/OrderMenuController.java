package bleuauction.bleuauction_be.server.orderMenu.controller;


import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberComponentService;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.repository.OrderMenuRepository;
import bleuauction.bleuauction_be.server.orderMenu.service.OrderMenuComponentService;
import bleuauction.bleuauction_be.server.orderMenu.service.OrderMenuModuleService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ordermenu")
public class OrderMenuController {

    private final OrderMenuComponentService orderMenuComponentService;
    private final OrderMenuModuleService orderMenuModuleService;

    private final OrderMenuRepository orderMenuRepository;
    private final JwtUtils jwtUtils;
    private final MemberComponentService memberComponentService;

    // 등록
    @PostMapping
    public ResponseEntity<?> orderMenu(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpSession session,
            OrderMenuDTO orderMenuDTO)
            throws Exception {
        jwtUtils.verifyToken(authorizationHeader);
        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        Optional<Member> loginUser =
                memberComponentService.findByMemberNo(tokenMember.getMemberNo());

        Order order = (Order) session.getAttribute("order");

        return orderMenuComponentService.addOrderMenuDTO(loginUser.get(), order, orderMenuDTO);
    }

    // 주문 번호별 주문메뉴 조회, 둘 중에 하나 삭제
    @GetMapping("/{orderNo}")
    public List<OrderMenu> findOrderMenus(@PathVariable("orderNo") Long orderNo) {
        return orderMenuComponentService.findOrderMenusByOrderNoAndStatusY(orderNo);
    }

    // 주문별 주문 메뉴 조회
    @GetMapping("/order/{orderNo}")
    public List<OrderMenu> findOrderMenuDTOs(@PathVariable("orderNo") Long orderNo) {
        return orderMenuComponentService.findOrderMenuDTOsByOrderNo(orderNo);
    }

    // 삭제
    @DeleteMapping("/{orderMenuNo}")
    public ResponseEntity<String> deleteOrderMenu(@PathVariable("orderMenuNo") Long orderMenuNo) {
        orderMenuModuleService.deleteOrderMenu(orderMenuNo);
        return ResponseEntity.ok("OrderMenu deleted successfully");
    }

    // 디테일(수정)
    @GetMapping("/detail/{orderMenuNo}")
    public ResponseEntity<OrderMenu> detailOM(@PathVariable("orderMenuNo") Long orderMenuNo) {
        OrderMenu OM = orderMenuModuleService.findOne(orderMenuNo);
        return ResponseEntity.ok(OM);
    }

    @PutMapping("/update/{orderMenuNo}")
    public ResponseEntity<String> updateOM(
            @RequestBody OrderMenuDTO request, @PathVariable("orderMenuNo") Long orderMenuNo) {
        orderMenuComponentService.update(orderMenuNo, request);
        log.info("ordermenu/update");
        return ResponseEntity.ok("OrderMenu updated successfully");
    }
}
