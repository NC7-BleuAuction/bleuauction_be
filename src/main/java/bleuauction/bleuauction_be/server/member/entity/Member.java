package bleuauction.bleuauction_be.server.member.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.attach.entity.MemberAttach;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ba_member")
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_no")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull private String password;

    @NotNull private String name;

    @NotNull private String phone;

    @Embedded private Address address;

    @Embedded private BankAccount bankAccount;

    @Enumerated(STRING)
    private MemberCategory category;

    @Enumerated(STRING)
    private MemberStatus status;

    @CreationTimestamp private Timestamp regDatetime;

    @UpdateTimestamp private Timestamp mdfDatetime;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<MemberAttach> attaches = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Store> stores = new ArrayList<>();

    @Builder
    public Member(
            String email,
            String password,
            String name,
            String zipCode,
            String addr,
            String detailAddr,
            String phone,
            @Nullable String bankName,
            @Nullable String bankAccount,
            MemberCategory category,
            MemberStatus status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.status = status;
        this.bankAccount =
                BankAccount.builder().bankName(bankName).bankAccount(bankAccount).build();
        this.address = Address.builder().zipCode(zipCode).addr(addr).detailAddr(detailAddr).build();
    }
}
