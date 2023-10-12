package bleuauction.bleuauction_be.server.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateMemberRequest {

    private Long memberNo;

    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String memberEmail;

    @Size(min = 4, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String memberPwd;

    @Size(min = 2, max = 10)
    private String memberName;

    private String memberAddr;

    private String memberZipcode;

    private String memberDetailAddr;

    @Pattern(regexp = "(01[016789])-(\\d{3,4})-(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String memberPhone;

    @Size(min = 4, max = 10)
    private String memberBank;

    private String memberAccount;

    @UpdateTimestamp
    private LocalDateTime mdfDatetime;

    private MultipartFile profileImage;

}
