package bleuauction.bleuauction_be.notice.service;

import bleuauction.bleuauction_be.notice.entity.Notice;
import bleuauction.bleuauction_be.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;

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


  @Transactional(readOnly = true)
  public Notice findOne(Long noticeNo) {
    return noticeRepository.findOne(noticeNo);
  }


  //노티스 삭제(N)
  @Transactional
  public void deleteNotice(Long noticeNo) {
    Notice notice = noticeRepository.findOne(noticeNo);
    notice.delete();
  }

  //노티스 수정
  @Transactional
  public Notice update(Long noticeNo, String title, String content) {
    Notice notice = noticeRepository.findOne(noticeNo);
    notice.setNoticeTitle(title);
    notice.setNoticeContent(content);
    return notice;
  }



}
