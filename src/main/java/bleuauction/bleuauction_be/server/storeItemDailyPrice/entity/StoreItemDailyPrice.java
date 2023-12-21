package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ba_store_item_daily_price")
public class StoreItemDailyPrice {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "daily_price_no")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "store_no")
  private Store store;

  private int dailyPrice;

  @Enumerated(STRING)
  private ItemCode itemCode;

  @Enumerated(STRING)
  private ItemName itemName;

  @Enumerated(STRING)
  private ItemSize itemSize;

  @Enumerated(STRING)
  private OriginStatus originStatus;

  @Enumerated(STRING)
  private OriginPlaceStatus originPlaceStatus;

  @Enumerated(STRING)
  private WildFarmStatus wildFarmStatus;

  @Enumerated(STRING)
  private DailyPriceStatus dailyPriceStatus;

  private LocalDateTime daliyPriceDate;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Builder
  public StoreItemDailyPrice(Store store, int dailyPrice, ItemCode itemCode, ItemName itemName, ItemSize itemSize, OriginStatus originStatus, OriginPlaceStatus originPlaceStatus, WildFarmStatus wildFarmStatus, LocalDateTime daliyPriceDate, DailyPriceStatus dailyPriceStatus) {
    this.store = store;
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
