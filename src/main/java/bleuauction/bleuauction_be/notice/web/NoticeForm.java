package bleuauction.bleuauction_be.notice.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeForm {

  @NotEmpty(message = "제목과 내용은 필수입니다.")
  public String notice_title;
  public String notice_content;

}
