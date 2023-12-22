package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.dto.StoreItemDailyPriceInsertRequest;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemCode;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemName;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemSize;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginPlaceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.WildFarmStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.util.StoreItemDailyPriceEntityFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StoreItemDailyPriceServiceTest {
    @Mock private StoreItemDailyPriceRepository storeItemDailyPriceRepository;
    @InjectMocks private StoreItemDailyPriceModuleService storeItemDailyPriceModuleService;

    private final Long TEST_STORE_NO = 1L;

    @Test
    @DisplayName(
            "testSelectSidpList(): DailyPriceStatus.Y(\"사용중\") 인 상태의 가게별 품목 당일 싯가 객체가 있으면 모두 조회한다. ")
    void testSelectSidpList() throws Exception {
        // given
        Store store = Store.builder().build();
        store.setId(TEST_STORE_NO);
        List<StoreItemDailyPrice> mockSidpList =
                List.of(
                        StoreItemDailyPriceEntityFactory.of(
                                store,
                                1000,
                                ItemCode.S,
                                ItemName.FI,
                                ItemSize.S,
                                OriginStatus.D,
                                OriginPlaceStatus.WS,
                                WildFarmStatus.F,
                                DailyPriceStatus.Y),
                        StoreItemDailyPriceEntityFactory.of(
                                store,
                                2000,
                                ItemCode.S,
                                ItemName.SL,
                                ItemSize.M,
                                OriginStatus.D,
                                OriginPlaceStatus.ES,
                                WildFarmStatus.W,
                                DailyPriceStatus.Y),
                        StoreItemDailyPriceEntityFactory.of(
                                store,
                                3000,
                                ItemCode.S,
                                ItemName.SL,
                                ItemSize.L,
                                OriginStatus.I,
                                OriginPlaceStatus.JP,
                                WildFarmStatus.F,
                                DailyPriceStatus.Y));
        given(storeItemDailyPriceRepository.findAllByDailyPriceStatus(DailyPriceStatus.Y))
                .willReturn(mockSidpList);

        // when
        List<StoreItemDailyPrice> sidpList =
                storeItemDailyPriceModuleService.findAllByDailyPriceStatus(DailyPriceStatus.Y);

        // then
        assertEquals(mockSidpList, sidpList);
    }

    @Test
    @DisplayName("testAddStoreItemDailyPrice(): 가게별 품목 당일 싯가 등록에 성공한다.")
    void testAddStoreItemDailyPrice() {
        // given
        Store store = Store.builder().build();
        store.setId(TEST_STORE_NO);

        StoreItemDailyPriceInsertRequest sidpInsertRequest = new StoreItemDailyPriceInsertRequest();
        sidpInsertRequest.setDailyPrice(1000);
        sidpInsertRequest.setItemCode(ItemCode.S);
        sidpInsertRequest.setItemName(ItemName.FI);
        sidpInsertRequest.setItemSize(ItemSize.M);
        sidpInsertRequest.setOriginStatus(OriginStatus.D);
        sidpInsertRequest.setOriginPlaceStatus(OriginPlaceStatus.WS);
        sidpInsertRequest.setWildFarmStatus(WildFarmStatus.F);
        sidpInsertRequest.setDailyPriceStatus(DailyPriceStatus.Y);
        sidpInsertRequest.setRegDatetime(Timestamp.valueOf(LocalDateTime.now()));
        sidpInsertRequest.setMdfDatetime(Timestamp.valueOf(LocalDateTime.now()));

        StoreItemDailyPrice sidp = sidpInsertRequest.toEntity(store);
        sidp.setId(1L);

        given(storeItemDailyPriceRepository.save(any(StoreItemDailyPrice.class))).willReturn(sidp);

        // when
        StoreItemDailyPrice addStoreItemDailyPrice =
                storeItemDailyPriceModuleService.addStoreItemDailyPrice(sidp);

        // then
        verify(storeItemDailyPriceRepository, times(1)).save(any(StoreItemDailyPrice.class));
        assertEquals(sidp, addStoreItemDailyPrice);
    }
}
