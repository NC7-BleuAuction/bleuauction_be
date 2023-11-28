package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;


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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StoreItemDailyPriceServiceTest {
  @Mock
  private StoreItemDailyPriceRepository storeItemDailyPriceRepository;
  @InjectMocks
  private StoreItemDailyPriceService storeItemDailyPriceService;

  private final Long TEST_STORE_NO = 1L;


  @Test
  @DisplayName("testSelectSidpList(): DailyPriceStatus.Y(\"사용중\") 인 상태의 가게별 품목 당일 싯가 객체가 있으면 모두 조회한다. ")
  void testSelectSidpList() throws Exception {
    // given
    List<StoreItemDailyPrice> mockSidpList = List.of(
            StoreItemDailyPriceEntityFactory.of(TEST_STORE_NO, 1000, ItemCode.S, ItemName.FI, ItemSize.S, OriginStatus.D, OriginPlaceStatus.WS, WildFarmStatus.F, DailyPriceStatus.Y),
            StoreItemDailyPriceEntityFactory.of(TEST_STORE_NO, 2000, ItemCode.S, ItemName.SL, ItemSize.M, OriginStatus.D, OriginPlaceStatus.ES, WildFarmStatus.W, DailyPriceStatus.Y),
            StoreItemDailyPriceEntityFactory.of(TEST_STORE_NO, 3000, ItemCode.S, ItemName.SL, ItemSize.L, OriginStatus.I, OriginPlaceStatus.JP, WildFarmStatus.F, DailyPriceStatus.Y)
    );
    given(storeItemDailyPriceRepository.findAllByDailyPriceStatus(DailyPriceStatus.Y)).willReturn(Optional.of(mockSidpList));

    // when
    List<StoreItemDailyPrice> sidpList = storeItemDailyPriceService.selectSidpList();

    //then
    assertEquals(mockSidpList, sidpList);
  }

  @Test
  @DisplayName("testAddStoreItemDailyPrice(): 가게별 품목 당일 싯가 등록에 성공한다.")
  void testAddStoreItemDailyPrice() {
    // given
    StoreItemDailyPrice mockStoreItemDailyPrice = StoreItemDailyPriceEntityFactory.mockStoreItemDailyPrice;
    given(storeItemDailyPriceRepository.save(mockStoreItemDailyPrice)).willReturn(mockStoreItemDailyPrice);

    // when
    StoreItemDailyPrice addStoreItemDailyPrice = storeItemDailyPriceService.addStoreItemDailyPrice(mockStoreItemDailyPrice);

    //then
    assertEquals(mockStoreItemDailyPrice, addStoreItemDailyPrice);
  }
}
