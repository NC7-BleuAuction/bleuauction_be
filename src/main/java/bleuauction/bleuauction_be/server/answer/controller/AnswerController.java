package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnswerController {

  final int PAGE_ROW_COUNT = 2;
  private final CreateJwt createJwt;
  private final AnswerService answerService;

  @GetMapping("/api/answer/list")
  public ResponseEntity<?> answerList(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("reviewNo") Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("url ===========> /api/answer/list");
    log.info("reivewNo: " + reviewNo);
    log.info("startPage: " + startPage);

    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }
      Page<Answer> page = answerService.selectAnswerList(reviewNo, AnswerStatus.Y, startPage, PAGE_ROW_COUNT);

      List<Answer> answerList = page.getContent();
      long totalRows = page.getTotalElements(); // 전체 행 수
      int totalPages = page.getTotalPages(); // 전체 페이지 수

      Map<String, Object> answerMap = new HashMap<>();
      answerMap.put("answerList", answerList);
      answerMap.put("totalRows", totalRows);
      answerMap.put("totalPages", totalPages);

      log.info("answerMap: ", answerMap);

      return ResponseEntity.ok(answerMap);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/api/answer/add")
  public ResponseEntity<?> answerAdd(@RequestHeader("Authorization") String authorizationHeader, Answer answer) {
    log.info("url ===========> /api/answer/add");
    log.info("authorizationHeader: ", authorizationHeader);
    log.info("Answer: " + answer);


    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      Member m = new Member();
      m.setMemberNo(tokenMember.getMemberNo());
      answer.setMember(m);
      Answer insertAnswer = answerService.addAnswer(answer);

      return ResponseEntity.ok(insertAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/api/answer/update")
  public ResponseEntity<?> answerUpdate(@RequestHeader("Authorization") String authorizationHeader, Answer answer, Long memberNo) {
    log.info("url ===========> /api/answer/update");
    log.info("authorizationHeader: " + authorizationHeader);
    log.info("answer: " + answer);
    log.info("memberNo: " + memberNo);

    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      if (tokenMember.getMemberNo() != memberNo) {
        throw new Exception("답글 수정 권한이 없습니다!");
      }

      Member m = new Member();
      m.setMemberNo(tokenMember.getMemberNo());
      answer.setMember(m);

      Answer updateAnswer = answerService.updateAnswer(answer);
      return ResponseEntity.ok(updateAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }

  }


  @GetMapping("/api/answer/delete")
  public ResponseEntity<?> answerDelete(@RequestHeader("Authorization") String authorizationHeader, Long answerNo, Long memberNo) {
    log.info("url ===========> /api/answer/delete");
    log.info("authorizationHeader: " + authorizationHeader);
    log.info("answerNo: " + answerNo);
    log.info("memberNo: " + memberNo);

    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      if (tokenMember.getMemberNo() != memberNo) {
        throw new Exception("답글 삭제 권한이 없습니다!");
      }
      Answer deleteAnswer = answerService.deleteAnswer(answerNo);
      return ResponseEntity.ok(deleteAnswer);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }
}
