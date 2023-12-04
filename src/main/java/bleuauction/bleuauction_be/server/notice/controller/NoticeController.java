package bleuauction.bleuauction_be.server.notice.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.MemberComponentService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import bleuauction.bleuauction_be.server.notice.service.NoticeService;
import bleuauction.bleuauction_be.server.notice.web.NoticeForm;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bleuauction.bleuauction_be.server.member.entity.MemberCategory.A;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;
  private final EntityManager entityManager;
  private final MemberComponentService memberComponentService;
  private final NoticeRepository noticeRepository;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final CreateJwt createJwt;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachComponentService attachComponentService;


  //등록
  @GetMapping("/api/notice/new")
  public NoticeForm createForm() {
    NoticeForm noticeForm = new NoticeForm();
    return noticeForm;
  }

//
  // 등록 처리(관리자 회원)
  @PostMapping("/api/notice/new")
  @Transactional
  public ResponseEntity<?>  notice(@RequestHeader("Authorization") String  authorizationHeader, Notice notice, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) {

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberComponentService.findByMemberNo(tokenMember.getMemberNo());

    if(loginUser.get().getMemberCategory() == A) {
    notice.setMemberNo(loginUser.get());

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
  public ResponseEntity<?> deleteNotice(@RequestHeader("Authorization") String  authorizationHeader, @PathVariable("noticeNo") Long noticeNo) {
    Notice notice = noticeService.findOne(noticeNo);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberComponentService.findByMemberNo(tokenMember.getMemberNo());

    if(loginUser.get().getMemberCategory() == A) {

      // 사진 상태를 'N'으로 변경
      if (notice != null) {
        if (notice.getNoticeAttaches() != null && !notice.getNoticeAttaches().isEmpty()) {
          for (Attach attach : notice.getNoticeAttaches()) {
            attachComponentService.changeFileStatusDeleteByFileNo(attach.getFileNo());
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
  public ResponseEntity<?> fileNoticeDelete(@RequestHeader("Authorization") String  authorizationHeader, HttpSession session, @PathVariable Long fileNo) {
    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberComponentService.findByMemberNo(tokenMember.getMemberNo());

    if(loginUser.get().getMemberCategory() == A) {
      attachComponentService.changeFileStatusDeleteByFileNo(fileNo);
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
  public ResponseEntity<?> updateNotice(@RequestHeader("Authorization") String  authorizationHeader, HttpSession session, Notice notice,
          @PathVariable("noticeNo") Long noticeNo,
           @RequestParam(name = "noticeTitle") String noticeTitle,
           @RequestParam(name = "noticeContent") String noticeContent,
          @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) {

    Notice updatedNotice = noticeService.findOne(noticeNo);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberComponentService.findByMemberNo(tokenMember.getMemberNo());

    if(loginUser.get().getMemberCategory() == A) {

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
