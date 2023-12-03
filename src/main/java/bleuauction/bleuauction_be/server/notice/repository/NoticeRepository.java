package bleuauction.bleuauction_be.server.notice.repository;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByNoticeStatus(NoticeStatus status);

    Notice findByNoticeNo(Long noticeNo);
}
