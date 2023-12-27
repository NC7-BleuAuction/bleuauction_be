package bleuauction.bleuauction_be.server.storeItemDailyPrice.dto;


import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemCode;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemName;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.ItemSize;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginPlaceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.OriginStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.WildFarmStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreItemDailyPriceInsertRequest {
    private int dailyPrice;

    private Long storeId;

    @Enumerated(EnumType.STRING)
    private ItemCode itemCode;

    @Enumerated(EnumType.STRING)
    private ItemName itemName;

    @Enumerated(EnumType.STRING)
    private ItemSize itemSize;

    @Enumerated(EnumType.STRING)
    private OriginStatus originStatus;

    @Enumerated(EnumType.STRING)
    private OriginPlaceStatus originPlaceStatus;

    @Enumerated(EnumType.STRING)
    private WildFarmStatus wildFarmStatus;

    private LocalDateTime dailyPriceDate;

    private Timestamp regDatetime;

    private Timestamp mdfDatetime;

    private DailyPriceStatus dailyPriceStatus = DailyPriceStatus.Y;

    public StoreItemDailyPrice toEntity(Store store) {
        return StoreItemDailyPrice.builder()
                .store(store)
                .dailyPrice(this.dailyPrice)
                .itemCode(this.itemCode)
                .itemName(this.itemName)
                .itemSize(this.itemSize)
                .originStatus(this.originStatus)
                .originPlaceStatus(this.originPlaceStatus)
                .wildFarmStatus(this.wildFarmStatus)
                .daliyPriceDate(this.dailyPriceDate)
                .dailyPriceStatus(this.dailyPriceStatus)
                .build();
    }
}
