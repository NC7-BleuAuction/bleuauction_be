package bleuauction.bleuauction_be.server.notice.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_notice")
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeNo;

  private String noticeTitle;

  private String noticeContent;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="member_no")
  private Member memberNo;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private NoticeStatus noticeStatus; // 상태 [Y,N]

  @JsonIgnore
  @OneToMany(mappedBy = "noticeNo", cascade=CascadeType.ALL)
  private List<Attach> noticeAttaches = new ArrayList<>();

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setNoticeStatus(NoticeStatus.N);
  }

  // 이미지 추가를 위한 메서드
  public void addNoticeAttach(Attach attach) {
    noticeAttaches.add(attach);
    attach.setNoticeNo(this); // 이미지와 메뉴를 연결
  }
}
