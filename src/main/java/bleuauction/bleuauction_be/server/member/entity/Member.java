package bleuauction.bleuauction_be.server.member.entity;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

@Entity
@Data
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "ba_member")
@Component
public class Member {

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

    @LastModifiedDate
    @Column(name = "mdf_datetime")
    private Timestamp mdfDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
    private MemberStatus memberStatus;

  @JsonManagedReference
  @OneToMany(mappedBy = "memberNo")
  private List<Notice> notices = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "memberNo")
  private List<OrderMenu> OrderMenus= new ArrayList<>();

    @OneToMany(mappedBy = "memberNo", cascade = CascadeType.ALL)
    private List<Attach> memberAttaches = new ArrayList<>();

    public void addAttaches(Attach attach) {
        memberAttaches.add(attach);
        attach.setMemberNo(this);
    }
}

