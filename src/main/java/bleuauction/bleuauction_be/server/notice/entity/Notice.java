package bleuauction.bleuauction_be.server.notice.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@Table(name = "ba_notice")
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeNo;

  private String noticeTitle;

  private String noticeContent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="member_no")
  private Member member;

  @CreationTimestamp
  private LocalDateTime regDatetime;

  @UpdateTimestamp
  private LocalDateTime mdfDatetime;

  @Enumerated(EnumType.STRING)
  private NoticeStatus noticeStatus; // 상태 [Y,N]

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setNoticeStatus(NoticeStatus.N);
  }
}
