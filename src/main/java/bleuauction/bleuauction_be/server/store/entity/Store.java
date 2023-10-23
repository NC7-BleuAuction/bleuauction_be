package bleuauction.bleuauction_be.server.store.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_store")
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_no")
  private Long storeNo;


  @JsonManagedReference
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member_no")
  private Member memberNo;

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

  //@NotNull
//  @UpdateTimestamp
//  @Column(columnDefinition = "TIME DEFAULT '09:00:00'")
  private Time weekdayStartTime; // 평일 시작시간

  //@NotNull
//  @UpdateTimestamp
//  @Column(columnDefinition = "TIME DEFAULT '09:00:00'")
  private Time weekdayEndTime; // 평일 종료시간

  //@NotNull
//  @UpdateTimestamp
//  @Column(columnDefinition = "TIME DEFAULT '00:00:00'")
  private Time weekendStartTime; // 주말 시작시간

  //@NotNull
//  @UpdateTimestamp
//  @Column(columnDefinition = "TIME DEFAULT '00:00:00'")
  private Time weekendEndTime; //  주말 종료시간

  @Enumerated(EnumType.STRING)
  private UnsupportedType unsupportedType; // 주문 불가 유형

  @Enumerated(EnumType.STRING)
  @Column(name = "store_status", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
  private StoreStatus storeStatus;

  @Builder.Default
  @JsonManagedReference
  @OneToMany(mappedBy = "storeNo")
  private List<Menu> menus = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "storeNo", cascade = CascadeType.ALL)
  private List<Attach> storeAttaches = new ArrayList<>();

  public void addAttaches(Attach attach) {
    storeAttaches.add(attach);
    attach.setStoreNo(this);
  }
}