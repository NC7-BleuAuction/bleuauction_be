package bleuauction.bleuauction_be.server.order.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Table(name = "ba_order")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_no")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // Q:퀵배송, T:포장

    private int orderPrice;

    private String orderRequest;

    private String recipientPhone;

    private String recipientName;

    private String recipientZipcode;

    private String recipientAddr;

    private String recipientDetailAddr;

    @CreationTimestamp private Timestamp regDatetime;

    @UpdateTimestamp private Timestamp mdfDatetime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 상태 [Y,N]

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = LAZY, cascade = ALL)
    private Pay pay;

    // TODO : 실제 사용이 이뤄지진 않고 있슴, 확인바람 by.승현
    public int calculOrderPrice() {
        return this.orderMenus.stream()
                .filter(orderMenu -> orderMenu.getMenu() != null)
                .mapToInt(orderMenu -> orderMenu.getMenu().getMenuPrice())
                .sum();
    }

    // 비지니스 로직
    // 공지사항 삭제
    public void delete() {
        this.setOrderStatus(OrderStatus.N);
        this.orderMenus.forEach(OrderMenu::delete);
    }
}
