package bleuauction.bleuauction_be.server.order.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.member.entity.Address;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Table(name = "ba_order")
@NoArgsConstructor(access = PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member; // 주문자 정보

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_no")
    private Store store; // 주문한 가게 정보

    private int orderPrice;

    private String orderRequest;

    private String recipientPhone;

    private String recipientName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zipcode")),
        @AttributeOverride(name = "addr", column = @Column(name = "recipient_addr")),
        @AttributeOverride(name = "detailAddr", column = @Column(name = "recipient_detail_addr"))
    })
    private Address recipientAddress;

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // Q:퀵배송, T:포장

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 상태 [Y,N]

    @OneToOne(mappedBy = "order", fetch = LAZY, cascade = ALL)
    private Pay pay;

    @CreationTimestamp private Timestamp regDatetime;

    @UpdateTimestamp private Timestamp mdfDatetime;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Builder
    public Order(
            Member member,
            Store store,
            int orderPrice,
            String orderRequest,
            String recipientPhone,
            String recipientName,
            String recipientZipcode,
            String recipientAddr,
            String recipientDetailAddr,
            OrderType orderType,
            OrderStatus orderStatus,
            Pay pay) {
        this.member = member;
        this.store = store;
        this.orderPrice = orderPrice;
        this.orderRequest = orderRequest;
        this.recipientPhone = recipientPhone;
        this.recipientName = recipientName;
        this.recipientAddress =
                Address.builder()
                        .zipCode(recipientZipcode)
                        .addr(recipientAddr)
                        .detailAddr(recipientDetailAddr)
                        .build();
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.pay = pay;
    }

    public Long calculOrderPrice() {
        return this.orderMenus.stream()
                .filter(orderMenu -> orderMenu.getMenu() != null)
                .mapToLong(orderMenu -> orderMenu.getMenu().getPrice())
                .sum();
    }

    // ===  편의 메서드 ===
    /** 주문자 정보 변경 */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /** 주문한 가게 정보 변경 */
    public void setStore(Store store) {
        this.store = store;
        store.getOrders().add(this);
    }

    /**
     * 주문 주소 변경 (혹시 몰라서)
     *
     * @param recipientZipCode
     * @param recipientAddr
     * @param recipientDetailAddr
     */
    public void setRecipientAddress(
            String recipientZipCode, String recipientAddr, String recipientDetailAddr) {
        setRecipientAddress(
                Address.builder()
                        .zipCode(recipientZipCode)
                        .addr(recipientAddr)
                        .detailAddr(recipientDetailAddr)
                        .build());
    }

    // === 비지니스 로직 ===
    /** 주문 취소 */
    public void deleteOrder() {
        this.setOrderStatus(OrderStatus.N);
        this.orderMenus.forEach(OrderMenu::delete);
    }
}
