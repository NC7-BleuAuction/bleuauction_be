package bleuauction.bleuauction_be.server.order.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.order.dto.OrderDTO;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bleuauction.bleuauction_be.server.order.entity.OrderType.T;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderComponentServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderModuleService orderModuleService;

    @Mock
    private MemberModuleService memberModuleService;

    @InjectMocks private OrderComponentService orderComponentService;

    @Test
    @DisplayName("주문 수정")
    void testUpdateOrder() {
        // given
        Order existingOrder =
                Order.builder()
                        .orderType(T)
                        .orderPrice(10000)
                        .orderRequest("많이 주세요")
                        .recipientPhone("010-3499-4444")
                        .recipientName("박승현")
                        .recipientZipcode("9999")
                        .recipientAddr("서울시 강남구")
                        .recipientDetailAddr("502-1호")
                        .build();
        existingOrder.setId(2500L);

        // findOne 호출될 때 existingOrder를 리턴하도록 설정
        when(orderRepository.findById(existingOrder.getId()))
                .thenReturn(Optional.of(existingOrder));


        OrderDTO request =
                OrderDTO.builder()
                        .orderType(T)
                        .orderPrice(20000)
                        .orderRequest("수정 하쇼")
                        .recipientPhone("010-4444-4444")
                        .recipientName("seunghyun")
                        .recipientZipcode("5555")
                        .recipientAddr("수정주소")
                        .recipientDetailAddr("502-314")
                        .build();

        // when
        // 서비스의 update 메소드 호출
        ResponseEntity<String> responseEntity = orderComponentService.update(existingOrder.getId(), request);

        // then
        // 반환된 ResponseEntity의 상태 코드와 메시지를 확인
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Order updated successfully", responseEntity.getBody());
    }

    @Test
    @DisplayName("가게 별 주문 조회 - 로그인 문제")
    void testFindOrdersByMemberAndStore_NoOrders() {
        // given
        Long memberNo = 123L;
        Long storeNo = 1L;

        // 로그인한 사용자를 찾지 못하도록 설정
        when(memberModuleService.findById(memberNo))
                .thenThrow(new MemberNotFoundException(memberNo));

        // when && then
        assertThrows(
                MemberNotFoundException.class,
                () -> orderComponentService.findOrdersByMemberAndStore(memberNo, storeNo));
    }


    @Test
    @DisplayName("가게 별 주문 조회 - 주문이 있는 경우")
    void testFindOrdersByMemberAndStore_WithOrders() {

     final String TEST_MAIL = "test@test.com";
     final String TEST_PWD = "testpassword123!";
     final String TEST_NAME = "테스트 이름";

        // given
        Member mockSellerMember = MemberEntityFactory.mockSellerMember;
        Member mockNormalMember =
                MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        Store mockStore = StoreUtilFactory.of("노량진수산시장", "가게이름1", "111-11-11111");
        mockStore.setId(1L);
        mockStore.addMember(mockSellerMember);

        // 가짜 메뉴 리스트를 생성
        Menu menu1 =
                Menu.builder()
                        .store(mockStore)
                        .name("메뉴1")
                        .size(MenuSize.L)
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();

        Menu menu2 =
                Menu.builder()
                        .store(mockStore)
                        .name("메뉴2")
                        .size(MenuSize.L)
                        .price(40000)
                        .status(MenuStatus.Y)
                        .build();

        // 가짜 주문 리스트를 생성
        List<Order> fakeOrders = new ArrayList<>();

        Order order1 =
                Order.builder()
                        .member(mockNormalMember)
                        .store(mockStore)
                        .orderType(T)
                        .orderStatus(OrderStatus.Y)
                        .build();
        Order order2 =
                Order.builder()
                        .member(mockNormalMember)
                        .store(mockStore)
                        .orderType(T)
                        .orderStatus(OrderStatus.Y)
                        .build();
        fakeOrders.add(order1);
        fakeOrders.add(order2);

        List<OrderMenu> orderMenuList1 = new ArrayList<>();

        OrderMenu order1OrderMenu1 =
                OrderMenu.builder()
                        .member(mockNormalMember)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu1)
                        .orderMenuCount(5)
                        .build();
        OrderMenu order1OrderMenu2 =
                OrderMenu.builder()
                        .member(mockNormalMember)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu2)
                        .orderMenuCount(5)
                        .build();
        orderMenuList1.add(order1OrderMenu1);
        orderMenuList1.add(order1OrderMenu2);
        order1.setOrderMenus(orderMenuList1);

        List<OrderMenu> orderMenuList2 = new ArrayList<>();
        OrderMenu order2OrderMenu1 =
                OrderMenu.builder()
                        .member(mockNormalMember)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu1)
                        .orderMenuCount(5)
                        .build();
        OrderMenu order2OrderMenu2 =
                OrderMenu.builder()
                        .member(mockNormalMember)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu2)
                        .orderMenuCount(5)
                        .build();
        orderMenuList2.add(order2OrderMenu1);
        orderMenuList2.add(order2OrderMenu2);
        order2.setOrderMenus(orderMenuList2);

        // 로그인한 사용자를 찾았다고 설정
        when(memberModuleService.findById(mockSellerMember.getId())).thenReturn(mockSellerMember);

        // 주문이 있는 상황을 가정하고, 주문 리스트를 반환하도록 설정
        when(orderRepository.findAllByStoreAndOrderStatusOrderByRegDatetimeDesc(
                mockStore, OrderStatus.Y))
                .thenReturn(fakeOrders);

        // when
        List<Order> result =
                orderComponentService.findOrdersByMemberAndStore(
                        mockSellerMember.getId(), mockStore.getId());

        // then
        assertEquals(fakeOrders, result);
    }

    @Test
    @DisplayName("회원 별 주문 조회 - 로그인 문제")
    void testFindOrdersByMemberNo_NoOrders() {
        // given
        Long memberNo = 123L;

        // 로그인한 사용자를 찾지 못하도록 설정
        when(memberModuleService.findById(memberNo))
                .thenThrow(new MemberNotFoundException(memberNo));

        // when && then
        assertThrows(
                MemberNotFoundException.class, () -> orderComponentService.findOrdersByMemberNo(memberNo));
    }

    @Test
    @DisplayName("회원 별 주문 조회 - 주문이 있는 경우")
    void testFindOrdersByMemberNo_WithOrders() {

        final String TEST_MAIL = "test@test.com";
        final String TEST_PWD = "testpassword123!";
        final String TEST_NAME = "테스트 이름";

        // given
        Long memberNo = 123L;

        Member fakeLoginUser =
                MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        fakeLoginUser.setId(memberNo);

        Store mockStore = StoreUtilFactory.of("노량진수산시장", "가게이름1", "111-11-11111");
        mockStore.setId(1L);

        // 가짜 메뉴 리스트를 생성
        Menu menu1 =
                Menu.builder()
                        .store(mockStore)
                        .name("메뉴1")
                        .size(MenuSize.L)
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();

        Menu menu2 =
                Menu.builder()
                        .store(mockStore)
                        .name("메뉴2")
                        .size(MenuSize.L)
                        .price(40000)
                        .status(MenuStatus.Y)
                        .build();

        // 로그인한 사용자를 찾았다고 설정
        when(memberModuleService.findById(memberNo)).thenReturn(fakeLoginUser);

        // 가짜 주문 리스트를 생성
        List<Order> fakeOrders = new ArrayList<>();

        Order order1 =
                Order.builder()
                        .member(fakeLoginUser)
                        .store(mockStore)
                        .orderType(T)
                        .orderStatus(OrderStatus.Y)
                        .build();
        Order order2 =
                Order.builder()
                        .member(fakeLoginUser)
                        .store(mockStore)
                        .orderType(T)
                        .orderStatus(OrderStatus.Y)
                        .build();
        fakeOrders.add(order1);
        fakeOrders.add(order2);

        List<OrderMenu> orderMenuList1 = new ArrayList<>();

        OrderMenu order1OrderMenu1 =
                OrderMenu.builder()
                        .member(fakeLoginUser)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu1)
                        .orderMenuCount(5)
                        .build();
        OrderMenu order1OrderMenu2 =
                OrderMenu.builder()
                        .member(fakeLoginUser)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu2)
                        .orderMenuCount(5)
                        .build();
        orderMenuList1.add(order1OrderMenu1);
        orderMenuList1.add(order1OrderMenu2);
        order1.setOrderMenus(orderMenuList1);

        List<OrderMenu> orderMenuList2 = new ArrayList<>();
        OrderMenu order2OrderMenu1 =
                OrderMenu.builder()
                        .member(fakeLoginUser)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu1)
                        .orderMenuCount(5)
                        .build();
        OrderMenu order2OrderMenu2 =
                OrderMenu.builder()
                        .member(fakeLoginUser)
                        .orderMenuStatus(OrderMenuStatus.Y)
                        .order(order1)
                        .menu(menu2)
                        .orderMenuCount(5)
                        .build();
        orderMenuList2.add(order2OrderMenu1);
        orderMenuList2.add(order2OrderMenu2);
        order2.setOrderMenus(orderMenuList2);

        // 주문이 있는 상황을 가정하고, 주문 리스트를 반환하도록 설정
        when(orderRepository.findAllByMemberOrderByRegDatetimeDesc(fakeLoginUser))
                .thenReturn(fakeOrders);

        // when
        List<Order> result = orderComponentService.findOrdersByMemberNo(memberNo);

        // then
        assertEquals(fakeOrders, result);
    }

}