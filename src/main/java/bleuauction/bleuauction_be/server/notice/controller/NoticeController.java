package bleuauction.bleuauction_be.server.notice.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.service.NoticeService;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/notice")
public class NoticeController {

  private final NoticeService noticeService;
  private final MemberService memberService;
  private final CreateJwt createJwt;
  private final AttachService attachService;



  // 등록 처리(관리자 회원)
  @PostMapping("/new")
  @Transactional
  public ResponseEntity<?>  notice(@RequestHeader("Authorization") String  authorizationHeader, Notice notice, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) throws Exception{

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    noticeService.enroll(notice,multipartFiles,loginUser.get());
    log.info("notice/postnew");

    try {
      return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }


  //목록조회
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Notice> findNotices(){
      List<Notice> notices = noticeService.findNoticesByStatus(NoticeStatus.Y);
      return notices;
  }


// 삭제
  @DeleteMapping("/{noticeNo}")
  public ResponseEntity<?> deleteNotice(@RequestHeader("Authorization") String  authorizationHeader, @PathVariable("noticeNo") Long noticeNo) {
    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    noticeService.deleteNotice(noticeNo,loginUser.get());
    return ResponseEntity.ok("Notice deleted successfully");
  }

  //사진삭제
  @DeleteMapping("/deletefile/{fileNo}")
  public ResponseEntity<?> fileNoticeDelete(@RequestHeader("Authorization") String  authorizationHeader, HttpSession session, @PathVariable Long fileNo) {
    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    if(loginUser.get().getMemberCategory() == A) {
      attachService.changeFileStatusToDeleteByFileNo(fileNo);
      return ResponseEntity.ok("File deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }

  //디테일
  @GetMapping("/detail/{noticeNo}")
  public Notice detailNotice(@PathVariable("noticeNo") Long noticeNo) {
    return noticeService.findOne(noticeNo);
  }

  // 수정 처리
  @PutMapping("/update/{noticeNo}")
  public ResponseEntity<?> updateNotice(@RequestHeader("Authorization") String  authorizationHeader,
          @PathVariable("noticeNo") Long noticeNo,
          @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) throws Exception {
    Notice updatedNotice = noticeService.findOne(noticeNo);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Optional<Member> loginUser = memberService.findByMemberNo(tokenMember.getMemberNo());

    noticeService.update(updatedNotice,loginUser.get(),multipartFiles);

    log.info("notice/update");
    return ResponseEntity.ok("Notice updated successfully");

  }
}
