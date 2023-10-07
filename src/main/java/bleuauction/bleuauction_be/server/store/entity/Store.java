package bleuauction.bleuauction_be.server.store.entity;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_store")
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_no")
  private Long storeNo;

  @Column(name = "member_no")
  private Long memberNo;

  @NotNull
  private String marketName; // (수산)시장명

  @NotNull
  private String storeName; // 가게명

  @NotNull
  private String licenseNo;

  @NotNull
  private String storeZipcode;

  @NotNull
  private String storeAddr;

  @NotNull
  private String storeDetailAddr;

  @NotNull
  private Time weekdayStartTime; // 평일 시작시간

  @NotNull
  private Time weekdayEndTime; // 평일 종료시간

  @NotNull
  private Time weekendStartTime; // 주말 시작시간

  @NotNull
  private Time weekendEndTime; // 주말 종료시간

  @Enumerated(EnumType.STRING)
  @Column(name="order_type")
  private UnsupportedType unsupportedType; // 주문 불가 유형

  @Enumerated(EnumType.STRING)
  private StoreStatus storeStatus;

  @OneToMany(mappedBy = "storeNo")
  private List<Menu> menus = new ArrayList<>();

}