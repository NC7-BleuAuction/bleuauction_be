package bleuauction.bleuauction_be.server.member.entity;


import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Component
@DynamicInsert
@DynamicUpdate
@Table(name = "ba_member")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @NotNull
    @Column(name = "member_email", unique = true)
    private String memberEmail;

    @NotNull
    private String memberPwd;

    @NotNull
    private String memberName;

    @NotNull
    private String memberZipcode;

    @NotNull
    private String memberAddr;

    @NotNull
    private String memberDetailAddr;

    @NotNull
    private String memberPhone;

    @Nullable
    private String memberBank;

    @Nullable
    private String memberAccount;

    @Enumerated(EnumType.STRING)
    private MemberCategory memberCategory;

    @CreationTimestamp
    @Column(name = "reg_datetime")
    private Timestamp regDatetime;

    @UpdateTimestamp
    @Column(name = "mdf_datetime")
    private Timestamp mdfDatetime;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "memberNo")
    private List<Notice> notices = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "memberNo")
    private List<OrderMenu> OrderMenus = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "memberNo")
    private List<Attach> memberAttaches = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "memberNo")
    private List<Store> ownedStores = new ArrayList<>();

    public void addAttaches(Attach attach) {
        memberAttaches.add(attach);
        attach.setMemberNo(this);
    }

    @Builder
    public Member(String memberEmail, String memberPwd, String memberName, String memberZipcode, String memberAddr, String memberDetailAddr, String memberPhone, @Nullable String memberBank, @Nullable String memberAccount, MemberCategory memberCategory, MemberStatus memberStatus) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberZipcode = memberZipcode;
        this.memberAddr = memberAddr;
        this.memberDetailAddr = memberDetailAddr;
        this.memberPhone = memberPhone;
        this.memberBank = memberBank;
        this.memberAccount = memberAccount;
        this.memberCategory = memberCategory;
        this.memberStatus = memberStatus;
    }
}

