package bleuauction.bleuauction_be.notice.service;

import bleuauction.bleuauction_be.notice.domain.Notice;
import bleuauction.bleuauction_be.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private NoticeRepository noticeRepository;

  //노티스 등록
  @Transactional
  public Long enroll(Notice notice) {
    noticeRepository.save(notice);
    return notice.getNotice_no();
  }

  //노티스 전체 조회
  @Transactional(readOnly = true)
  public List<Notice> findNotices() {
    return noticeRepository.findAll();
  }


  @Transactional(readOnly = true)
  public Notice findOne(int notice_no) {
    return noticeRepository.findOne(notice_no);
  }


  //노티스 삭제(N)
  @Transactional
  public void deleteNotice(int notice_no) {
    Notice notice = noticeRepository.findOne(notice_no);
    notice.delete();
  }




}
