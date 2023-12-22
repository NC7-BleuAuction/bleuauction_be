package bleuauction.bleuauction_be.server.store.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.attach.entity.StoreAttach;
import bleuauction.bleuauction_be.server.member.entity.Address;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ba_store")
public class Store {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "store_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @NotNull private String marketName; // (수산)시장명

    @NotNull private String storeName; // 가게명

    @NotNull private String licenseNo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "zipCode", column = @Column(name = "store_zipcode")),
        @AttributeOverride(name = "addr", column = @Column(name = "store_addr")),
        @AttributeOverride(name = "detailAddr", column = @Column(name = "store_detail_addr"))
    })
    private Address storeAddress;

    private Time weekdayStartTime; // 평일 시작시간

    private Time weekdayEndTime; // 평일 종료시간

    private Time weekendStartTime; // 주말 시작시간

    private Time weekendEndTime; //  주말 종료시간

    @Enumerated(STRING)
    private UnsupportedType unsupportedType; // 주문 불가 유형

    @Enumerated(STRING)
    private StoreStatus status;

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<StoreItemDailyPrice> storeItemDailyPrices = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<StoreAttach> attaches = new ArrayList<>();

    @Builder
    public Store(
            Member member,
            @NotNull String marketName,
            @NotNull String storeName,
            @NotNull String licenseNo,
            @NotNull String storeZipcode,
            @NotNull String storeAddr,
            @NotNull String storeDetailAddr,
            Time weekdayStartTime,
            Time weekdayEndTime,
            Time weekendStartTime,
            Time weekendEndTime,
            UnsupportedType unsupportedType,
            StoreStatus status) {
        this.member = member;
        this.marketName = marketName;
        this.storeName = storeName;
        this.licenseNo = licenseNo;
        this.storeAddress =
                Address.builder()
                        .zipCode(storeZipcode)
                        .addr(storeAddr)
                        .detailAddr(storeDetailAddr)
                        .build();
        this.weekdayStartTime = weekdayStartTime;
        this.weekdayEndTime = weekdayEndTime;
        this.weekendStartTime = weekendStartTime;
        this.weekendEndTime = weekendEndTime;
        this.unsupportedType = unsupportedType;
        this.status = status;
    }

    public void setStoreAddress(String storeZipcode, String storeAddr, String storeDetailAddr) {
        this.storeAddress =
                Address.builder()
                        .zipCode(storeZipcode)
                        .addr(storeAddr)
                        .detailAddr(storeDetailAddr)
                        .build();
    }

    public void changeStoreStatusN() {
        this.setStatus(StoreStatus.N);
    }

    public void changeStoreStatusY() {
        this.setStatus(StoreStatus.Y);
    }

    // === 연관관계 메서드 ===

    /**
     * Store와 Order는 1:N 관계이므로 Store가 Menu를 가지고 있어야 합니다.
     *
     * @param order
     */
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setStore(this);
    }

    /** Store와 Member는 1:N 관계이므로 Store가 Menu를 가지고 있어야 합니다. */
    public void addMember(Member member) {
        this.member = member;
        member.getStores().add(this);
    }
}
