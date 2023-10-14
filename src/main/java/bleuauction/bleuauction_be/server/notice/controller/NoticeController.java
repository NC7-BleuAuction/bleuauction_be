package bleuauction.bleuauction_be.server.notice.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.service.NoticeService;
import bleuauction.bleuauction_be.server.notice.web.NoticeForm;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;
  private final EntityManager entityManager;


  //등록
  @GetMapping("/api/notice/new")
  public NoticeForm createForm() {
    NoticeForm noticeForm = new NoticeForm();
    return noticeForm;
  }


  // 등록 처리
  @PostMapping("/api/notice/new")
  @Transactional
  public ResponseEntity<String>  notice(@Valid @RequestBody NoticeForm form) {
    Notice notice = new Notice();
    Member member =entityManager.find(Member.class, 1L);
    notice.setNoticeTitle(form.getNoticeTitle());
    notice.setNoticeContent(form.getNoticeContent());
    notice.setMember(member);
    notice.setNoticeStatus(NoticeStatus.Y);
    notice = entityManager.merge(notice);
    noticeService.enroll(notice); // 서비스 메소드에서 엔터티 등록

    log.info("notice/postnew");

    //return "redirect:/notices";
    return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");
  }


  //목록조회
  @GetMapping(value = "/api/notice", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Notice> findNotices() throws Exception {
    try {
      List<Notice> notices = noticeService.findNotices();
      return notices;
    } catch (Exception e) {
      // 예외 처리 코드 추가
      e.printStackTrace(); // 또는 로깅 등의 작업을 수행하세요.
      return new ArrayList<>();
    }
  }


// 삭제 처리
  @PostMapping("/api/notice/delete/{noticeNo}")
  public ResponseEntity<String> deleteNotice(@PathVariable("noticeNo") Long noticeNo) {
    noticeService.deleteNotice(noticeNo);
    return ResponseEntity.ok("Notice deleted successfully");
  }

  //디테일(수정)
  @GetMapping("/api/notice/detail/{noticeNo}")
  public ResponseEntity<Notice> detailNotice(@PathVariable("noticeNo") Long noticeNo) {
    Notice notice = noticeService.findOne(noticeNo);
    if (notice != null) {
      return ResponseEntity.ok(notice);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // 수정 처리
  @PostMapping("/api/notice/update/{noticeNo}")
  public ResponseEntity<String> updateNotice(
          @PathVariable("noticeNo") Long noticeNo,
          @RequestBody NoticeForm form
  ) {
    Notice updated = noticeService.update(noticeNo, form.getNoticeTitle(), form.getNoticeContent());
    if (updated!= null) {
      return ResponseEntity.ok("Notice updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notice not found or update failed");
    }
  }

}
