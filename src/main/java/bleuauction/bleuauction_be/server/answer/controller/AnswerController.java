package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.service.StoreService;
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

  private final AnswerService answerService;

  @GetMapping("/api/answer/list")
  public Map<String, Object> answerList(@RequestParam("reviewNo") Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) throws Exception {
    log.info("url ===========> /api/answer/list");
    log.info("reivewNo: " + reviewNo);
    log.info("startPage: " + startPage);


    Page<Answer> page = answerService.selectAnswerList(reviewNo, AnswerStatus.Y, startPage, PAGE_ROW_COUNT);

    List<Answer> answerList = page.getContent();
    long totalRows = page.getTotalElements(); // 전체 행 수
    int totalPages = page.getTotalPages(); // 전체 페이지 수

    Map<String, Object> answerMap = new HashMap<>();
    answerMap.put("answerList", answerList);
    answerMap.put("totalRows", totalRows);
    answerMap.put("totalPages", totalPages);

    log.info("answerMap: " + answerMap);

    return answerMap;
  }

  @PostMapping("/api/answer/add")
  public Answer answerAdd(HttpSession session, Answer answer) throws Exception {
    log.info("url ===========> /api/answer/add");
    log.info("Answer: " + answer);

    Member loginUser = (Member) session.getAttribute("loginUser");

    answer.setMember(loginUser);

    Answer insertAnswer = answerService.addAnswer(answer);
    return insertAnswer;
  }

  @PostMapping("/api/answer/update")
  public ResponseEntity<?> answerUpdate(HttpSession session, Answer answer, Member member) throws Exception {
    log.info("url ===========> /api/answer/update");

    Optional<Member> loginUserOptional = Optional.ofNullable((Member) session.getAttribute("loginUser"));
    Member loginUser = loginUserOptional.orElseThrow(() -> new Exception("로그인 유저가 없습니다!"));
    log.info("loginUser: " + loginUser);

    if (loginUser.getMemberNo() != member.getMemberNo()) {
      throw new Exception("답글 수정 권한이 없습니다!");
    }

    try {
      answer.setMember(loginUser);
      log.info("Answer: " + answer);

      Answer updateAnswer = answerService.updateAnswer(answer);
      return ResponseEntity.ok(updateAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }

  }


  @GetMapping("/api/answer/delete")
  public Answer answerDelete(@RequestParam("answerNo") Long answerNo) throws Exception {
    log.info("url ===========> /api/answer/delete");
    log.info("answerNo: " + answerNo);
    Answer deleteAnswer = answerService.deleteAnswer(answerNo);
    return deleteAnswer;
  }
}
