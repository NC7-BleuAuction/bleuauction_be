package bleuauction.bleuauction_be.server.orderMenu.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_order_menu")
public class OrderMenu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderMenuNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="memberNo")
  private Member memberNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="menuNo")
  private Menu menuNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="orderNo")
  private Order orderNo;

  private int orderMenuCount;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private OrderMenuStatus orderMenuStatus; // 상태 [Y,N]
}