package bleuauction.bleuauction_be.server.orderMenu.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.common.entity.AbstractTimeStamp;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
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

@Getter
@Setter
@Entity
@Table(name = "ba_order_menu")
@NoArgsConstructor(access = PROTECTED)
public class OrderMenu extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_menu_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_no")
    private Menu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    private int orderMenuCount;

    @Enumerated(EnumType.STRING)
    private OrderMenuStatus orderMenuStatus; // 상태 [Y,N]

    @Builder
    public OrderMenu(
            Member member,
            Menu menu,
            Order order,
            int orderMenuCount,
            OrderMenuStatus orderMenuStatus) {
        this.member = member;
        this.menu = menu;
        this.order = order;
        this.orderMenuCount = orderMenuCount;
        this.orderMenuStatus = orderMenuStatus;
    }

    // 비지니스 로직
    // 공지사항 삭제
    public void delete() {
        this.setOrderMenuStatus(OrderMenuStatus.N);
    }

    public void setOrder(Order order) {
        this.order = order;
        order.getOrderMenus().add(this);
    }
}
