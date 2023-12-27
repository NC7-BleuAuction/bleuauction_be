package bleuauction.bleuauction_be.server.order.controller;


import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.order.dto.MemberFindOrdersResponseDto;
import bleuauction.bleuauction_be.server.order.dto.OrderDTO;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.service.OrderComponentService;
import bleuauction.bleuauction_be.server.order.service.OrderModuleService;
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
@RequestMapping("/api/order")
public class OrderController {

    private final OrderModuleService orderModuleService;
    private final OrderComponentService orderComponentService;
    private final JwtUtils jwtUtils;

    // 등록
    @PostMapping("/new")
    public ResponseEntity<?> addorder(Order order, HttpSession session) throws Exception {
        log.info("order/postnew");
        session.setAttribute("order", order);
        return orderModuleService.addOrder(order);
    }

    // 회원별 주문 조회
    @GetMapping
    public ResponseEntity<MemberFindOrdersResponseDto> findOrders(
            @RequestHeader("Authorization") String authorizationHeader) {
        jwtUtils.verifyToken(authorizationHeader);

        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        log.info("token: " + tokenMember);

        return ResponseEntity.ok(
                MemberFindOrdersResponseDto.builder()
                        .orders(orderComponentService.findOrdersByMemberNo(tokenMember.getMemberNo()))
                        .build());
    }

    // 가게(가게주인)별 주문 조회
    @GetMapping("/store/{storeNo}")
    public ResponseEntity<List<Order>> findOrdersbyStore(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("storeNo") Long storeNo) {
        jwtUtils.verifyToken(authorizationHeader);

        TokenMember tokenMember = jwtUtils.getTokenMember(authorizationHeader);
        log.info("token: " + tokenMember);

        return ResponseEntity.ok(
                orderComponentService.findOrdersByMemberAndStore(tokenMember.getMemberNo(), storeNo));
    }

    // 삭제--오더메뉴도 같이
    @PostMapping("/delete/{orderNo}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderNo") Long orderNo) {
        return orderModuleService.deleteOrder(orderNo);
    }

    // 디테일(수정)
    @GetMapping("detail/{orderNo}")
    public ResponseEntity<?> detailOrder(
            HttpSession session, @PathVariable("orderNo") Long orderNo) {
        Optional<Order> order = orderModuleService.findOne(orderNo);
        session.setAttribute("order", order);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/update/{orderNo}")
    public ResponseEntity<String> updateOrder(@PathVariable("orderNo") Long orderNo,
                                              @RequestBody OrderDTO orderDTO) {
        log.info("order/update");
        return orderComponentService.update(orderNo, orderDTO);
    }
}
