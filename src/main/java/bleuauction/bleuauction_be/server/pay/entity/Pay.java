package bleuauction.bleuauction_be.server.pay.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ba_pay")
public class Pay {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "pay_no")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @NotNull private Integer price;

    @Enumerated(EnumType.STRING)
    private PayStatus status;

    @CreationTimestamp
    private Timestamp payDatetime;

    @LastModifiedDate
    private Timestamp payCancelDatetime;

    @Builder
    public Pay(
            Order order,
            PayType payType,
            Integer price,
            PayStatus status,
            Timestamp payDatetime,
            Timestamp payCancelDatetime) {
        this.order = order;
        this.payType = payType;
        this.price = price;
        this.status = status;
        this.payDatetime = payDatetime;
        this.payCancelDatetime = payCancelDatetime;
    }
}
