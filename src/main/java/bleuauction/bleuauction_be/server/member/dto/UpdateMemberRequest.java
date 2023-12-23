package bleuauction.bleuauction_be.server.member.dto;


import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateMemberRequest {

    private Long memberNo;
    private TokenMember tokenMember;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String memberPwd;

    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 10)
    private String memberName;

    @NotNull(message = "기본주소는 필수 입력값입니다.")
    private String memberAddr;

    @NotNull(message = "우편번호는 필수 입력값입니다.")
    private String memberZipcode;

    @NotNull(message = "상세주소는 필수 입력값입니다.")
    private String memberDetailAddr;

    @NotNull(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "(01[016789])-(\\d{3,4})-(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String memberPhone;

    @Size(min = 4, max = 10)
    private String memberBank;

    private String memberAccount;

    private MultipartFile profileImage;
}
