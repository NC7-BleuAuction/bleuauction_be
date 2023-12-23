package bleuauction.bleuauction_be.server.attach.entity;

import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("NOTICE")
public class NoticeAttach extends Attach {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_no")
    private Notice notice;

    public NoticeAttach(AttachVO attachVO) {
        super(attachVO);
    }

    public NoticeAttach(AttachVO attachVO, Notice notice) {
        this(attachVO);
        setNotice(notice);
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
        notice.getAttaches().add(this);
    }
}
