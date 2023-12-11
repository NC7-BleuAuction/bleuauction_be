package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_store_item_daily_price")
public class StoreItemDailyPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dailyPriceNo;

  private Long storeNo;

  private int dailyPrice;

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

  private Date daliyPriceDate;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private DailyPriceStatus dailyPriceStatus;

  @Builder
  public StoreItemDailyPrice(Long storeNo, int dailyPrice, ItemCode itemCode, ItemName itemName, ItemSize itemSize, OriginStatus originStatus, OriginPlaceStatus originPlaceStatus, WildFarmStatus wildFarmStatus, Date daliyPriceDate, DailyPriceStatus dailyPriceStatus) {
    this.storeNo = storeNo;
    this.dailyPrice = dailyPrice;
    this.itemCode = itemCode;
    this.itemName = itemName;
    this.itemSize = itemSize;
    this.originStatus = originStatus;
    this.originPlaceStatus = originPlaceStatus;
    this.wildFarmStatus = wildFarmStatus;
    this.daliyPriceDate = daliyPriceDate;
    this.dailyPriceStatus = dailyPriceStatus;
  }
}
