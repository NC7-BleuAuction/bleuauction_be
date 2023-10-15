package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnswerController {

  final int PAGE_ROW_COUNT = 2;

  private final AnswerService answerService;

  @GetMapping("/answer/list")
  public List<Answer> answerList(@RequestParam("reviewNo") Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) throws Exception {
    log.info("url ===========> /answer/list");
    log.info("reivewNo: " + reviewNo);
    log.info("startRow: " + startPage);
    List<Answer> answerList = answerService.selectAnswerList(reviewNo, AnswerStatus.Y, startPage, PAGE_ROW_COUNT);
    log.info("answerList: " + answerList);

    return answerList;
  }

  @PostMapping("/answer/add")
  public Answer answerAdd(Answer answer) throws Exception {
    log.info("url ===========> /answer/add");
    log.info("Answer: " + answer);

    answer.setMemberNo(1L); // 로그인 구현되면 Session에서 받아오도록 수정요망!

    Answer insertAnswer = answerService.addAnswer(answer);
    return insertAnswer;
  }

  @PostMapping("/answer/update")
  public Answer answerUpdate(Answer answer) throws Exception {
    log.info("url ===========> /answer/update");
    log.info("Answer: " + answer);
    Answer updateAnswer = answerService.updateAnswer(answer);
    return updateAnswer;
  }


  @GetMapping("/answer/delete")
  public Answer answerDelete(@RequestParam("answerNo") Long answerNo) throws Exception {
    log.info("url ===========> /delete/sendAxios");
    log.info("answerNo: " + answerNo);
    Answer deleteAnswer = answerService.deleteAnswer(answerNo);
    return deleteAnswer;
  }
}
