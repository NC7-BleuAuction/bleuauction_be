package bleuauction.bleuauction_be.server.notice.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import bleuauction.bleuauction_be.server.attach.entity.NoticeAttach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notice_no")
    private Long id;

    private String noticeTitle;

    @Lob private String noticeContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @CreationTimestamp private Timestamp regDatetime;

    @UpdateTimestamp private Timestamp mdfDatetime;

    @Enumerated(STRING)
    private NoticeStatus noticeStatus; // 상태 [Y,N]

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeAttach> attaches = new ArrayList<>();

    // 비지니스 로직
    // 공지사항 삭제
    public void delete() {
        this.setNoticeStatus(NoticeStatus.N);
    }
}
