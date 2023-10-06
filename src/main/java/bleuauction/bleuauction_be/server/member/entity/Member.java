package bleuauction.bleuauction_be.server.member.entity;



        import bleuauction.bleuauction_be.server.notice.entity.Notice;
        import jakarta.persistence.*;

        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;

        import lombok.AccessLevel;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.hibernate.annotations.CurrentTimestamp;
        import org.hibernate.annotations.UpdateTimestamp;
        import org.jetbrains.annotations.NotNull;
        import org.jetbrains.annotations.Nullable;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_member")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_no")
  private Long memberNo;

  @NotNull
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

  @CurrentTimestamp
  @Column(name = "reg_datetime")
  private LocalDateTime regDatetime;

  @UpdateTimestamp
  @Column(name = "mdf_datetime")
  private LocalDateTime mdfDatetime;

  @Enumerated(EnumType.STRING)
  private MemberStatus memberStatus;

  @OneToMany(mappedBy = "member")
  private List<Notice> notices = new ArrayList<>();
}