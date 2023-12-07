package bleuauction.bleuauction_be.server.common.jwt;

import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenMember {
    private Long memberNo;
    private String memberEmail;
    private String memberName;
    private MemberCategory memberCategory;

    @Builder
    public TokenMember(Long memberNo, String memberEmail, String memberName, MemberCategory memberCategory) {
        this.memberNo = memberNo;
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.memberCategory = memberCategory;
    }

    public static TokenMember of(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return TokenMember.builder()
                .memberNo(Long.parseLong(decodedJWT.getSubject()))
                .memberEmail(decodedJWT.getClaim("memberEmail").asString())
                .memberName(decodedJWT.getClaim("memberName").asString())
                .memberCategory(MemberCategory.valueOf(decodedJWT.getClaim("memberCategory").asString()))
                .build();
    }
}

