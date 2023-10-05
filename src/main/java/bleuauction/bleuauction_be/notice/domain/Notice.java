package bleuauction.bleuauction_be.notice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Entity
@Getter
@Setter
@Table(name = "notice")
public class Notice {

  @Id
  @GeneratedValue
  @Column
  private Long notice_no;
  private String notice_title;
  private String notice_content;

//  @ManyToOne
//  @JoinColumn(name ="member_no")
//  private Member member_no;
  private DateTime reg_datetiem;
  private DateTime mdf_datetime;

  @Enumerated(EnumType.STRING)
  private NoticeStatus notice_status; // 상태 [Y,N]


  //비지니스로직
  //공지사항 삭제
  public void delete(){
    this.setNotice_status(NoticeStatus.N);
  }

}
