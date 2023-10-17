package bleuauction.bleuauction_be.server.store.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
import org.hibernate.annotations.DynamicInsert;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
@Slf4j
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_store")
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_no")
  private Long storeNo;

  @OneToOne
  @JoinColumn(name = "member_no", referencedColumnName = "memberNo")
  private Member memberNo;

  public void setMember(Member member) {
    this.memberNo = member;
  }

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

  @JsonManagedReference
  @OneToMany(mappedBy = "storeNo")
  private List<Menu> menus = new ArrayList<>();

  @OneToMany(mappedBy = "storeNo", cascade = CascadeType.ALL)
  private List<Attach> storeAttaches = new ArrayList<>();

  public void addAttaches(Attach attach) {
    storeAttaches.add(attach);
    attach.setStoreNo(this);
  }
}