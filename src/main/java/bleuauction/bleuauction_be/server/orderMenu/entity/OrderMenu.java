package bleuauction.bleuauction_be.server.orderMenu.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "ba_order_menu")
public class OrderMenu {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long orderMenuNo;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name ="member_no")
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name ="menu_no")
  private Menu menu;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name ="order_no")
  private Order order;

  private int orderMenuCount;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private OrderMenuStatus orderMenuStatus; // 상태 [Y,N]

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setOrderMenuStatus(OrderMenuStatus.N);
  }

  public void setOrder(Order order) {
    this.order = order;
    order.getOrderMenus().add(this);
  }
}
