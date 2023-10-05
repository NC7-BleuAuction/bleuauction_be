package bleuauction.bleuauction_be.notice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "ba_notice")
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="notice_no")
  private Long noticeNo;

  @Column(name="notice_title")
  private String noticeTitle;
  @Column(name="notice_content")
  private String noticeContent;

  @ManyToOne
  @JoinColumn(name ="member_no")
  private Member member_no;

  @CurrentTimestamp
  @Column(name="reg_datetiem")
  private LocalDateTime regDatetime;
  @CurrentTimestamp
  @Column(name="mdf_datetime")
  private LocalDateTime mdfDatetime;

  @Enumerated(EnumType.STRING)
  @Column(name="notice_status")
  private NoticeStatus noticeStatus; // 상태 [Y,N]


  //비지니스로직
  //공지사항 삭제
  public void delete(){
    this.setNoticeStatus(NoticeStatus.N);
  }

}
