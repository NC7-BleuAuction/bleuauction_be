package bleuauction.bleuauction_be.server.notice.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.service.NoticeService;
import bleuauction.bleuauction_be.server.notice.web.NoticeForm;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

import static bleuauction.bleuauction_be.server.member.entity.MemberCategory.A;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;
  private final EntityManager entityManager;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;


  //등록
  @GetMapping("/api/notice/new")
  public NoticeForm createForm() {
    NoticeForm noticeForm = new NoticeForm();
    return noticeForm;
  }


  // 등록 처리(관리자 회원)
  @PostMapping("/api/notice/new")
  @Transactional
  public ResponseEntity<String>  notice(HttpSession session, Notice notice, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) {
    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser.getMemberCategory() == A) {
    notice.setMemberNo(loginUser);

    if (multipartFiles != null && multipartFiles.size() > 0) {
      ArrayList<Attach> attaches = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFiles) {
        if (multipartFile.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                  "bleuauction-bucket", "notice/", multipartFile);
          notice.addNoticeAttach(attach);
        }
      }
    }

    notice = entityManager.merge(notice);
    noticeService.enroll(notice);

    log.info("notice/postnew");

    return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");}
    else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }


  //목록조회
  @GetMapping(value = "/api/notice", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Notice> findNotices() throws Exception {
    try {
      List<Notice> notices = noticeService.findNoticesByStatus(NoticeStatus.Y);
      return notices;
    } catch (Exception e) {
      // 예외 처리 코드 추가
      e.printStackTrace(); // 또는 로깅 등의 작업을 수행하세요.
      return new ArrayList<>();
    }
  }


// 삭제
  @PostMapping("/api/notice/delete/{noticeNo}")
  public ResponseEntity<String> deleteNotice(HttpSession session, @PathVariable("noticeNo") Long noticeNo) {
    Notice notice = noticeService.findOne(noticeNo);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if(loginUser.getMemberCategory() == A) {

      // 사진 상태를 'N'으로 변경
      if (notice != null) {
        if (notice.getNoticeAttaches() != null && !notice.getNoticeAttaches().isEmpty()) {
          for (Attach attach : notice.getNoticeAttaches()) {
            attachService.update(attach.getFileNo());
          }
        }

        noticeService.deleteNotice(noticeNo);
        return ResponseEntity.ok("Notice deleted successfully");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notice not found");

      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }

  //사진삭제
  @DeleteMapping("/api/notice/deletefile/{fileNo}")
  public ResponseEntity<String> fileNoticeDelete(HttpSession session, @PathVariable Long fileNo) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if(loginUser.getMemberCategory() == A) {
      attachService.update(fileNo);
      return ResponseEntity.ok("File deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }



  //디테일(수정)
  @GetMapping("/api/notice/detail/{noticeNo}")
  public ResponseEntity<Notice> detailNotice(@PathVariable("noticeNo") Long noticeNo) {
    Notice notice = noticeService.findOne(noticeNo);

    if (notice != null) {
      notice.setNoticeAttaches(notice.getNoticeAttaches());
      return ResponseEntity.ok(notice);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // 수정 처리
  @PostMapping("/api/notice/update/{noticeNo}")
  public ResponseEntity<String> updateNotice(HttpSession session, Notice notice,
          @PathVariable("noticeNo") Long noticeNo,
           @RequestParam(name = "noticeTitle") String noticeTitle,
           @RequestParam(name = "noticeContent") String noticeContent,
          @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) {

    Notice updatedNotice = noticeService.findOne(noticeNo);
    Member loginUser = (Member) session.getAttribute("loginUser");

    if (loginUser.getMemberCategory() == A) {

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "notice/", multipartFile);
            updatedNotice.addNoticeAttach(attach);
          }
        }
      }
      // 공지사항 정보 업데이트
      updatedNotice.setNoticeTitle(noticeTitle);
      updatedNotice.setNoticeContent(noticeContent);

      noticeService.update(updatedNotice);

      log.info("notice/update");
      return ResponseEntity.ok("Notice updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }
}
