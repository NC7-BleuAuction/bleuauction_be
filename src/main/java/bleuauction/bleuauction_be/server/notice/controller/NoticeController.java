package bleuauction.bleuauction_be.server.notice.controller;

import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.service.NoticeModuleService;
import bleuauction.bleuauction_be.server.notice.service.NoticeComponentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final MemberModuleService memberModuleService;
  private final CreateJwt createJwt;
  private final AttachService attachService;
  private final NoticeModuleService noticeModuleService;
  private final NoticeComponentService noticeComponentService;



  // 등록 처리(관리자 회원)
  @PostMapping
  @Transactional
  public ResponseEntity<?>  notice(@RequestHeader("Authorization") String authorizationHeader, Notice notice, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) throws Exception{

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

    noticeComponentService.enroll(notice,multipartFiles,loginUser);
    log.info("notice/postnew");

    return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");
  }


  //목록조회
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Notice> findNotices(){
      return noticeModuleService.findNoticesByStatus(NoticeStatus.Y);
  }


// 삭제
  @DeleteMapping("/{noticeNo}")
  public ResponseEntity<String> deleteNotice(@RequestHeader("Authorization") String  authorizationHeader, @PathVariable("noticeNo") Long noticeNo) {
    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

    noticeComponentService.deleteNotice(noticeNo,loginUser);
    return ResponseEntity.ok("Notice deleted successfully");
  }

  //사진삭제
  @DeleteMapping("/file/{fileNo}")
  public ResponseEntity<String> fileNoticeDelete(@RequestHeader("Authorization") String  authorizationHeader, @PathVariable Long fileNo) {
    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

    if(MemberCategory.A.equals(loginUser.getMemberCategory())) {
      attachService.changeFileStatusToDeleteByFileNo(fileNo);
      return ResponseEntity.ok("File deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }

  //디테일
  @GetMapping("/{noticeNo}")
  public Notice detailNotice(@PathVariable("noticeNo") Long noticeNo) {
    return noticeModuleService.findOne(noticeNo);
  }

  // 수정 처리
  @PutMapping("/{noticeNo}")
  public ResponseEntity<String> updateNotice(@RequestHeader("Authorization") String  authorizationHeader,
          @PathVariable("noticeNo") Long noticeNo,
          @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) throws Exception {
   // Notice updatedNotice = noticeModuleService.findOne(noticeNo);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

    noticeComponentService.update(noticeNo,loginUser,multipartFiles);

    return ResponseEntity.ok("Notice updated successfully");

  }


}
