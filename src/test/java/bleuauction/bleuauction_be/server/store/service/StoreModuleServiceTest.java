package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreModuleServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreModuleService storeModuleService;

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
                () -> storeModuleService.findById(1L)
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
        Store foundStore = storeModuleService.findById(1L);

        // then
        assertEquals(store, foundStore);
    }


    @Test
    @DisplayName("Member를 기준으로 가게정보를 조회할 때, 존재하지 않으면 StoreNotFoundException이 발생한다.")
    void findStoreByMember_givenStoreNoAndNotExists_thenThrowStoreNotFoundException() {
        // given
        Member mockMember = MemberEntityFactory.mockSellerMember;
        StoreNotFoundException expectException = new StoreNotFoundException(mockMember);
        given(storeRepository.findByMemberNo(mockMember)).willReturn(Optional.empty());

        // when && then
        StoreNotFoundException e = assertThrows(
                StoreNotFoundException.class,
                () -> storeModuleService.findByMember(mockMember)
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
        Store foundStore =  storeModuleService.findByMember(mockMember);

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
        Page<Store> foundList = storeModuleService.findPageByStoreStatus(StoreStatus.Y, PageRequest.of(0, 3));

        //then
        assertEquals(mockPage, foundList);
    }
}