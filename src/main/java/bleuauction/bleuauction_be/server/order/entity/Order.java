package bleuauction.bleuauction_be.server.order.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ba_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long OrderNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="store_no")
  private Store storeNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="member_no")
  private Member memberNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="menu_no")
  private Menu menuNo;

  //쿠폰 등록 후 추가
  //private Coupon couponNo;

  private Long orderPrice;

  private Long orderCount;

  private String orderRequest;

  private String recipientPhone;

  private String recipientName;

  private String recipientZipcode;

  private String recipientAddr;

  private Long recipientDetailAddr;

  @CreationTimestamp
  private LocalDateTime orderDatetime;

  @UpdateTimestamp
  private LocalDateTime orderCancelDatetime;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
  private OrderStatus orderStatus; // 상태 [Y,N]


}
