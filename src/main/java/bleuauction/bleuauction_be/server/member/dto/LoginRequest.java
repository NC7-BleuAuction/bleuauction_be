package bleuauction.bleuauction_be.server.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Getter
@Setter
public class LoginRequest {

  @NotNull(message = "이메일은 필수 입력값 입니다.")
  private String memberEmail;

  @NotNull(message = "비밀번호는 필수 입력값 입니다.")
  @Size(min = 4, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
  private String memberPwd;
}
