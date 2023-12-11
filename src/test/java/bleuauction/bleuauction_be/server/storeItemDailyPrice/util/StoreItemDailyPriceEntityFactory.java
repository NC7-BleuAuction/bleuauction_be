package bleuauction.bleuauction_be.server.storeItemDailyPrice.util;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemCode;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemName;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemSize;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginPlaceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.WildFarmStatus;

public class StoreItemDailyPriceEntityFactory {
  public static final StoreItemDailyPrice mockStoreItemDailyPrice;

  static {
    StoreItemDailyPrice storeItemDailyPrice = StoreItemDailyPrice.builder()
            .storeNo(1L)
            .dailyPrice(1000)
            .itemCode(ItemCode.S)
            .itemName(ItemName.FI)
            .itemSize(ItemSize.M)
            .originStatus(OriginStatus.D)
            .originPlaceStatus(OriginPlaceStatus.WS)
            .wildFarmStatus(WildFarmStatus.F)
            .dailyPriceStatus(DailyPriceStatus.Y)
            .build();
    storeItemDailyPrice.setDailyPriceNo(1L);

    mockStoreItemDailyPrice = storeItemDailyPrice;
  }


  public static StoreItemDailyPrice of(Long storeNo, int dailyPrice, ItemCode itemCode, ItemName itemName, ItemSize itemSize, OriginStatus originStatus, OriginPlaceStatus originPlaceStatus, WildFarmStatus wildFarmStatus, DailyPriceStatus dailyPriceStatus) {
    return StoreItemDailyPrice.builder()
            .storeNo(storeNo)
            .dailyPrice(dailyPrice)
            .itemCode(itemCode)
            .itemName(itemName)
            .itemSize(itemSize)
            .originStatus(originStatus)
            .originPlaceStatus(originPlaceStatus)
            .wildFarmStatus(wildFarmStatus)
            .dailyPriceStatus(dailyPriceStatus)
            .build();
  }
}
