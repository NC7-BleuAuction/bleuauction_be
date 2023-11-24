package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private final String TEST_MARKETNAME = "노량진 쑤산시장";
    private final String TEST_STORENAME = "블루오크션";
    private final String TEST_LICENSE = "111-111-111111";

    @Test
    @DisplayName("Id를 기준으로 가게정보를 조회할 때, 존재하지 않으면 StoreNotFoundException이 발생한다.")
    void findStoreById_givenStoreNoAndNotExists_thenThrowStoreNotFoundException() {
        // given
        StoreNotFoundException expectException = new StoreNotFoundException(1L);
        given(storeRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // when && then
        StoreNotFoundException e = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.findStoreById(1L)
        );
        assertEquals(expectException.getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("Id를 기준으로 가게정보를 조회할 때, 존재하는 경우에는 Store객체를 반환한다.")
    void findStoreById_givenStoreNoAndExists_thenReturnStore() {
        // given
        Store store = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        store.setStoreNo(1L);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        Store foundStore = storeService.findStoreById(1L);

        // then
        assertEquals(store, foundStore);
    }

    @Test
    @DisplayName("Member를 기준으로 가게정보를 조회할 때, 존재하지 않으면 StoreNotFoundException이 발생한다.")
    void findStoreByMember_givenStoreNoAndNotExists_thenThrowStoreNotFoundException() {
        // given
        Member mockMember = MemberEntityFactory.mockSellerMember;
        StoreNotFoundException expectException = new StoreNotFoundException(mockMember.getMemberNo());
        given(storeRepository.findByMemberNo(mockMember)).willReturn(Optional.empty());

        // when && then
        StoreNotFoundException e = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.findStoreByMember(mockMember)
        );
        assertEquals(expectException.getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("Member를 기준으로 가게정보를 조회할 때, 존재하는 경우에는 Store객체를 반환한다.")
    void findStoreByMember_givenStoreNoAndExists_thenReturnStore() {
        // given
        Member mockMember = MemberEntityFactory.mockSellerMember;
        Store store = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        store.setStoreNo(1L);
        given(storeRepository.findByMemberNo(mockMember)).willReturn(Optional.of(store));

        // when
        Store foundStore = storeService.findStoreByMember(mockMember);

        // then
        assertEquals(store, foundStore);
    }

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

        given(storeRepository.findAllByStoreStatus(any(StoreStatus.class), any(Pageable.class))).willReturn(mockPage);

        // when
        List<Store> foundList = storeService.selectStoreList(StoreStatus.Y, 0, 3);

        //then
        assertEquals(mockReturnList, foundList);
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 일반회원인 경우 Exception이 발생한다.")
    void signup_givenMemberIsNormalMember_thenThrowIllegalAccessException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.M);

        // when && then
        IllegalAccessException e = assertThrows(
                IllegalAccessException.class,
                () -> storeService.signup(new StoreSignUpRequest(), mockMember)
        );
        assertEquals("판매자 회원만 등록 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("가게를 등록 할 때, 파라미터로 제공받은 Member가 관리자회원인 경우 Exception이 발생한다.")
    void signup_givenMemberIsAdmin_thenThrowIllegalAccessException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.A);

        // when && then
        IllegalAccessException e = assertThrows(
                IllegalAccessException.class,
                () -> storeService.signup(new StoreSignUpRequest(), mockMember)
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

        given(storeRepository.existsStoreByStoreName(mockRequest.getStoreName())).willReturn(true);

        // when && then
        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> storeService.signup(mockRequest, mockMember)
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

        given(storeRepository.existsStoreByStoreName(mockRequest.getStoreName())).willReturn(false);
        given(storeRepository.save(any(Store.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        storeService.signup(mockRequest, mockMember);

        // then
        InOrder inOrder = inOrder(storeRepository);
        // 몇번 호출하였는지 검증
        inOrder.verify(storeRepository, times(1)).existsStoreByStoreName(mockRequest.getStoreName());
        //최소 1번 이상 호출되었는지 검증
        inOrder.verify(storeRepository, atLeast(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 정보 삭제를 할 때, 가게 소유주가 아닌 경우 StoreUpdateUnAuthorizedException이 발생한다")
    void widhDrawStore_givenMemberIsNotStoreOwner_thenThrowStoreUpdateUnAuthorizedException() {
        // given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        Store mockStore = StoreUtilFactory.of("노량진수산시장", "블루크오션", "111-11-11111");
        mockStore.setStoreNo(1L);

        // when && then
        StoreUpdateUnAuthorizedException e = assertThrows(
                StoreUpdateUnAuthorizedException.class,
                () -> storeService.withDrawStore(mockStore, mockMember)
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

        given(storeRepository.save(mockStore)).willReturn(mockStore);

        // when
        storeService.withDrawStore(mockStore, mockMember);

        // then
        assertEquals(StoreStatus.N, mockStore.getStoreStatus());
        InOrder inOrder = inOrder(storeRepository);
        inOrder.verify(storeRepository, times(1)).save(mockStore);
    }
}