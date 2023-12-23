package bleuauction.bleuauction_be.server.member.dto;


import bleuauction.bleuauction_be.server.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Member loginUser;
}
