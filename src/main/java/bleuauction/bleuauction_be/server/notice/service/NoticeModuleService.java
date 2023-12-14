package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class NoticeModuleService {

    private final NoticeRepository noticeRepository;
    @Transactional(readOnly = true)
    public Notice findOne(Long noticeNo) {
        return noticeRepository.findByNoticeNo(noticeNo);
    }

    public List<Notice> findNoticesByStatus(NoticeStatus status) {
        return noticeRepository.findByNoticeStatus(status);
    }

    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }
}
