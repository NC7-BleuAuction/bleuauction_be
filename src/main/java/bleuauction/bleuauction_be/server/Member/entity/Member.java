package bleuauction.bleuauction_be.server.Member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String memberEmail;

    @NotNull
    private String memberPwd;

    @NotNull
    private String memberName;

    @NotNull
    private String memberDetailAddr;

    @NotNull
    private String memberPhone;

    @Nullable
    private String memberBank;

    @Nullable
    private String memberAccount;

    @Nullable
    private String memberCategory;

    @Nullable
    private DateTime regDatetime;

    @Nullable
    private DateTime mdfDatetime;

    @Nullable
    private String memberStatus;

    // TODO: address_no 컬럼 나중에 추가하기

}
