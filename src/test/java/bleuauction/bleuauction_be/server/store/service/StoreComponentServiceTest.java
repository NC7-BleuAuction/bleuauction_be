package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class StoreComponentServiceTest {

    @Mock
    private StoreModuleService storeModuleService;

    @Mock
    private AttachComponentService attachComponentService;

    @InjectMocks
    private StoreComponentService storeComponentService;

    private final String TEST_MARKETNAME = "노량진 쑤산시장";
    private final String TEST_STORENAME = "블루오크션";
    private final String TEST_LICENSE = "111-111-111111";

    @Test
    @DisplayName("가게 리스트를 페이지조회시 Limit그리고 Status를 파라미터로 제공 할 때, 해당 Limit, Page에 맞춰 결과물을 반환한다.")
    void selectStoreList_givenStatusAndPageAndLimit_thenReturnStorePageList() {
        //given
        List<Store> mockReturnList = List.of(
                StoreUtilFactory.of("노량진수산시장", "가게이름1", "111-11-11111"),
                StoreUtilFactory.of("노량진수산시장", "가게이름2", "111-11-11112"),
                StoreUtilFactory.of("노량진수산시장", "가게이름3", "111-11-11113")
        );
        Page<Store> mockPage = new PageImpl<>(mockReturnList, PageRequest.of(0, 3), 5L);

        given(storeModuleService.findPageByStoreStatus(any(StoreStatus.class), any(Pageable.class))).willReturn(mockPage);

        // when
        List<Store> foundList = storeComponentService.selectStoreList(StoreStatus.Y, 0, 3);

        //then
        assertEquals(mockReturnList, foundList);
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 일반회원인 경우 Exception이 발생한다.")
    void signup_givenMemberIsNormalMember_thenThrowIllegalAccessException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.M);

        // when && then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> storeComponentService.signup(new StoreSignUpRequest(), mockMember)
        );
        assertEquals("판매자 회원만 등록 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 관리자회원인 경우 Exception이 발생한다.")
    void signup_givenMemberIsAdmin_thenThrowIllegalAccessException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.A);

        // when && then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> storeComponentService.signup(new StoreSignUpRequest(), mockMember)
        );
        assertEquals("판매자 회원만 등록 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 판매자회원이나, 이미 존재하는 가게인 경우 Exception이 발생한다.")
    void signup_givenMemberIsSellerButExistsStoreName_thenThrowIllegalAccessException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        StoreSignUpRequest mockRequest = new StoreSignUpRequest();
        mockRequest.setMarketName("노량진수산시장");
        mockRequest.setStoreName("블루크오션");
        mockRequest.setLicenseNo("111-11-11111");
        mockRequest.setStoreZipcode("14055");
        mockRequest.setStoreAddr("경기도 안양시 동안구 경수대로 623번길");
        mockRequest.setStoreDetailAddr("어딘가의 빌딩5층");
        mockRequest.setWeekdayStartTime(Time.valueOf(LocalTime.of(9, 0, 0)));
        mockRequest.setWeekdayEndTime(Time.valueOf(LocalTime.of(23, 0, 0)));
        mockRequest.setWeekendStartTime(Time.valueOf(LocalTime.of(11, 0, 0)));
        mockRequest.setWeekendEndTime(Time.valueOf(LocalTime.of(23, 59, 59)));

        given(storeModuleService.isExistByStoreName(mockRequest.getStoreName())).willReturn(true);

        // when && then
        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> storeComponentService.signup(mockRequest, mockMember)
        );
        assertEquals("이미 존재하는 가게 입니다.", e.getMessage());
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 판매자회원이면서, 동일한 가게명이 없는 경우 정상적으로 실행된다.")
    void signup_givenMemberIsSellerAndIsNotExistsStoreName_thenSaveIsSuccess() throws IllegalAccessException {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        StoreSignUpRequest mockRequest = new StoreSignUpRequest();
        mockRequest.setMarketName("노량진수산시장");
        mockRequest.setStoreName("블루크오션");
        mockRequest.setLicenseNo("111-11-11111");
        mockRequest.setStoreZipcode("14055");
        mockRequest.setStoreAddr("경기도 안양시 동안구 경수대로 623번길");
        mockRequest.setStoreDetailAddr("어딘가의 빌딩5층");
        mockRequest.setWeekdayStartTime(Time.valueOf(LocalTime.of(9, 0, 0)));
        mockRequest.setWeekdayEndTime(Time.valueOf(LocalTime.of(23, 0, 0)));
        mockRequest.setWeekendStartTime(Time.valueOf(LocalTime.of(11, 0, 0)));
        mockRequest.setWeekendEndTime(Time.valueOf(LocalTime.of(23, 59, 59)));

        given(storeModuleService.isExistByStoreName(mockRequest.getStoreName())).willReturn(false);
        given(storeModuleService.save(any(Store.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        storeComponentService.signup(mockRequest, mockMember);

        // then
        InOrder inOrder = inOrder(storeModuleService);
        // 몇번 호출하였는지 검증
        inOrder.verify(storeModuleService, times(1)).isExistByStoreName(mockRequest.getStoreName());
        //최소 1번 이상 호출되었는지 검증
        inOrder.verify(storeModuleService, atLeast(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 정보 삭제를 할 때, 가게 소유주가 아닌 경우 StoreUpdateUnAuthorizedException이 발생한다")
    void widhDrawStore_givenMemberIsNotStoreOwner_thenThrowStoreUpdateUnAuthorizedException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        Store mockStore = StoreUtilFactory.of("노량진수산시장", "블루크오션", "111-11-11111");
        mockStore.setStoreNo(1L);

        given(storeModuleService.findById(mockStore.getStoreNo())).willReturn(mockStore);

        // when && then
        StoreUpdateUnAuthorizedException e = assertThrows(
                StoreUpdateUnAuthorizedException.class,
                () -> storeComponentService.withDrawStore(mockStore.getStoreNo(), mockMember)
        );
        assertTrue(e.getMessage().startsWith("[StoreUpdateUnAuthorizedException] UnAuthorized"));
    }

    @Test
    @DisplayName("가게 정보 삭제를 할 때, 가게 소유주가 제공받은 Member객체인 경우, 정상적으로 폐업상태로 변경된다.")
    void widhDrawStore_givenMemberIsStoreOwner_thenUpdateIsSuccess() {
        // given
        Member mockMember = MemberEntityFactory.mockSellerMember;
        Store mockStore = StoreUtilFactory.of("노량진수산시장", "블루크오션", "111-11-11111");
        mockStore.setStoreNo(1L);

        given(storeModuleService.findById(mockStore.getStoreNo())).willReturn(mockStore);
        given(storeModuleService.save(mockStore)).willReturn(mockStore);

        // when
        storeComponentService.withDrawStore(mockStore.getStoreNo(), mockMember);

        // then
        assertEquals(StoreStatus.N, mockStore.getStoreStatus());
        InOrder inOrder = inOrder(storeModuleService);
        inOrder.verify(storeModuleService, times(1)).save(mockStore);
    }
}