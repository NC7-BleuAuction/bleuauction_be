package bleuauction.bleuauction_be.server.pay.entity;

import bleuauction.bleuauction_be.server.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "ba_pay")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_no")
    private Long payNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="order_no")
    private Order orderNo;

    @NotNull
    private String payType;

    @NotNull
    private Integer payPrice;

    @CreationTimestamp
    @Column(name = "pay_datetime")
    private Timestamp payDatetime;

    @LastModifiedDate
    @Column(name = "pay_cancel_datetime")
    private Timestamp payCancelDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PayStatus payStatus;

}
