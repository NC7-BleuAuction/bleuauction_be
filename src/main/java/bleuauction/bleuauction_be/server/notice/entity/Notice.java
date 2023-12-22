package bleuauction.bleuauction_be.server.notice.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.attach.entity.NoticeAttach;
import bleuauction.bleuauction_be.server.common.entity.AbstractTimeStamp;
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
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ba_notice")
@NoArgsConstructor(access = PROTECTED)
public class Notice extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notice_no")
    private Long id;

    private String noticeTitle;

    @Lob private String noticeContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Enumerated(STRING)
    private NoticeStatus noticeStatus; // 상태 [Y,N]

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeAttach> attaches = new ArrayList<>();

    @Builder
    public Notice(
            String noticeTitle, String noticeContent, Member member, NoticeStatus noticeStatus) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.member = member;
        this.noticeStatus = noticeStatus;
    }

    // 비지니스 로직
    // 공지사항 삭제
    public void delete() {
        this.setNoticeStatus(NoticeStatus.N);
    }
}
