package bleuauction.bleuauction_be.server.notice.web;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeForm {

  @NotEmpty(message = "제목과 내용은 필수입니다.")
@Column(name="notice_title")
public String noticeTitle;
  @Column(name="notice_content")
  public String noticeContent;

}
