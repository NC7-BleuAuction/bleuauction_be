package bleuauction.bleuauction_be.server.common.jwt;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
@ToString
public class APIUserDTO extends User {

  private Long memberNo;
  private String memberEmail;
  private String memberPwd;
  private String memberName;
  private MemberCategory memberCategory;

  public APIUserDTO(Long memberNo, String memberEmail, String memberPwd, String memberName, MemberCategory memberCategory) {
    super(memberEmail, memberPwd, Collections.singleton(new SimpleGrantedAuthority(memberCategory.name())));
    this.memberNo = memberNo;
    this.memberEmail = memberEmail;
    this.memberPwd = memberPwd;
    this.memberName = memberName;
    this.memberCategory = memberCategory;
  }

  public Member toMemberEntity() {
    Member member = Member.builder()
            .memberEmail(memberEmail)
            .memberPwd(memberPwd)
            .memberName(memberName)
            .memberCategory(memberCategory)
            .build();
    member.setMemberNo(memberNo);
    return member;
  }
}
