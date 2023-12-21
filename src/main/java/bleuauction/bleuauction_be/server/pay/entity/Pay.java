package bleuauction.bleuauction_be.server.pay.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ba_pay")
public class Pay {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "pay_no")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="order_no")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @NotNull
    private Integer payPrice;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @CreationTimestamp
    @Column(name = "pay_datetime")
    private Timestamp payDatetime;

    @LastModifiedDate
    @Column(name = "pay_cancel_datetime")
    private Timestamp payCancelDatetime;

    @Builder
    public Pay(Order order, PayType payType, Integer payPrice, PayStatus payStatus, Timestamp payDatetime, Timestamp payCancelDatetime){
        this.order = order;
        this.payType = payType;
        this.payPrice = payPrice;
        this.payStatus = payStatus;
        this.payDatetime = payDatetime;
        this.payCancelDatetime = payCancelDatetime;
    }
}
