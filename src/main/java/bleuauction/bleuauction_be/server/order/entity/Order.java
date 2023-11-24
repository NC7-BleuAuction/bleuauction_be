package bleuauction.bleuauction_be.server.order.entity;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderNo;

  @Enumerated(EnumType.STRING)
  private OrderType orderType; // Q:퀵배송, T:포장

  private int orderPrice;

  private String orderRequest;

  private String recipientPhone;

  private String recipientName;

  private String recipientZipcode;

  private String recipientAddr;

  private String recipientDetailAddr;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus; // 상태 [Y,N]

  @JsonManagedReference
  @OneToMany(mappedBy = "orderNo", cascade = CascadeType.ALL)
  private List<OrderMenu> orderMenus= new ArrayList<>();

  public int calculateOrderPrice() {
    int totalPrice = 0;

    for (OrderMenu orderMenu : orderMenus) {
      Menu menu = orderMenu.getMenuNo();
      if (menu != null) {
        totalPrice += menu.getMenuPrice();
      }
    }

    return totalPrice;
  }


  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setOrderStatus(OrderStatus.N);

    for (OrderMenu orderMenu : this.getOrderMenus()) {
      orderMenu.delete(); // 주문 메뉴 상태 변경
    }
  }
}
