package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository1 noticeRepository;

  @Transactional
  public Long enroll(Notice notice) {
    noticeRepository.save(notice);
    return notice.getNoticeNo();
  }

  //노티스 전체 조회
  @Transactional(readOnly = true)
  public List<Notice> findNotices() {
    return noticeRepository.findAll();
  }

  public List<Notice> findNoticesByStatus(NoticeStatus status) {
    return noticeRepository.findByNoticeStatus(status);
  }


  @Transactional(readOnly = true)
  public Notice findOne(Long noticeNo) {
    return noticeRepository.findByNoticeNo(noticeNo);
  }


  //노티스 삭제(N)
  @Transactional
  public void deleteNotice(Long noticeNo) {
    Notice notice = noticeRepository.findByNoticeNo(noticeNo);
    notice.delete();
  }

  //노티스 수정
  @Transactional
  public Notice update(Notice notice) {
    Notice updatenotice = noticeRepository.findByNoticeNo(notice.getNoticeNo());

    updatenotice.setNoticeTitle(notice.getNoticeTitle());
    updatenotice.setNoticeContent(notice.getNoticeContent());
    return updatenotice;
  }



}
