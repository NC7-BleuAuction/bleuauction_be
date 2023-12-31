package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class APIUserDTO extends User {

    private Long memberNo;
    private String memberEmail;
    private String memberPwd;
    private String memberName;
    private MemberCategory memberCategory;

    public APIUserDTO(
            Long memberNo,
            String memberEmail,
            String memberPwd,
            String memberName,
            MemberCategory memberCategory) {
        super(
                memberEmail,
                memberPwd,
                Collections.singleton(new SimpleGrantedAuthority(memberCategory.name())));
        this.memberNo = memberNo;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberCategory = memberCategory;
    }

    public Member toMemberEntity() {
        Member member =
                Member.builder()
                        .email(memberEmail)
                        .password(memberPwd)
                        .name(memberName)
                        .category(memberCategory)
                        .build();
        member.setId(memberNo);
        return member;
    }
}
