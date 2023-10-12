package bleuauction.bleuauction_be.server.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
public class UpdateMemberRequest {

    private Long memberNo;

    @NotBlank(message = "수정할 이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "수정할 비밀번호를 입력해주세요.")
    @Size(min = 4, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String memberPwd;

    @NotBlank(message = "수정할 이름을 입력해주세요.")
    @Size(min = 2, max = 10)
    private String memberName;

    @NotBlank(message = "수정할 기본주소를 입력해주세요.")
    private String memberAddr;

    @NotBlank(message = "우편번호는 자동 등록됩니다.")
    private String memberZipcode;

    @NotBlank(message = "수정할 상세주소를 입력해주세요.")
    private String memberDetailAddr;

    @NotBlank(message = "수정할 휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])-(\\d{3,4})-(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String memberPhone;

    @NotBlank(message = "수정할 은형명을 입력해주세요.")
    @Size(min = 4, max = 10)
    private String memberBank;

    @NotBlank(message = "수정할 계좌번호를 입력해주세요.")
    private String memberAccount;

    @UpdateTimestamp
    private LocalDateTime mdfDatetime;


}
