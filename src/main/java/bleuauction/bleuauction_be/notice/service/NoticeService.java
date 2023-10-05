package bleuauction.bleuauction_be.notice.service;

import bleuauction.bleuauction_be.notice.entity.Notice;
import bleuauction.bleuauction_be.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

  @Autowired
  private NoticeRepository noticeRepository;


  //노티스 등록
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
  public Notice findOne(Long notice_no) {
    return noticeRepository.findOne(notice_no);
  }


  //노티스 삭제(N)
  @Transactional
  public void deleteNotice(Long noticeNo) {
    Notice notice = noticeRepository.findOne(noticeNo);
    notice.delete();
  }




}
