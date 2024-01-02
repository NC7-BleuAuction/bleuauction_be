package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.order.dto.OrderDTO;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.exception.OrderNotFoundException;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class OrderComponentService {


    private MemberModuleService memberModuleService;
    private OrderRepository orderRepository;
    private OrderModuleService orderModuleService;

    // 회원 별 주문 조회
    public List<Order> findOrdersByMemberNo(Long memberNo) {
        Member loginMember = memberModuleService.findById(memberNo);

        return orderRepository.findAllByMemberOrderByRegDatetimeDesc(loginMember);
    }



    /**
     * 가게에 주문이 들어온 목록에 대해서 조회가 필요하기 떄문에 다음과 같이 구현하였습니다.
     *
     * @param memberNo
     * @param storeNo
     * @return
     */
    public List<Order> findOrdersByMemberAndStore(Long memberNo, Long storeNo) {
        Member storeMember = memberModuleService.findById(memberNo);

        // TODO : Exception 적당한걸로 생성한 이후에 바꿔주세요 By.승현
        if (!MemberCategory.isMemberSeller(storeMember.getCategory())) {
            throw new IllegalArgumentException("판매자가 아닙니다");
        }

        // TODO : Exception 적당한걸로 바꿔주세요 By.승현
        Store findStore =
                storeMember.getStores().stream()
                        .filter(store -> store.getId().equals(storeNo))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("가게 주인이 아닙니다."));

        return orderRepository.findAllByStoreAndOrderStatusOrderByRegDatetimeDesc(
                findStore, OrderStatus.Y);
    }



    // TODO : 기능은 이전과 동일하게 만들었는데, 조회를 위한 NO만 있고, 수정을 하는 정보도 모두 DB에서 가져와서 기능이 아얘 없는거나 마찬가지. 수정필요
    // By.승현
    // 메뉴 수정
    public ResponseEntity<String> update(Long orderNo, OrderDTO request) {
        Order existingorder =
                orderRepository
                        .findById(orderNo)
                        .orElseThrow(() -> new OrderNotFoundException(orderNo));

        existingorder.setOrderType(request.getOrderType());
        existingorder.setOrderPrice(request.getOrderPrice()); // 필요하다면 주석 해제
        existingorder.setOrderRequest(request.getOrderRequest());
        existingorder.setRecipientPhone(request.getRecipientPhone());
        existingorder.setRecipientName(request.getRecipientName());
        existingorder.setRecipientAddress(request.buildRecipientAddress());

        orderModuleService.addOrder(existingorder);

        return ResponseEntity.ok("Order updated successfully");
    }
}
