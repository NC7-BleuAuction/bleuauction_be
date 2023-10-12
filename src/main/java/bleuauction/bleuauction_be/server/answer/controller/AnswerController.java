package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class AnswerController {

  final int PAGE_ROW_COUNT = 2;

  @Autowired
  AnswerService answerService;

  @GetMapping("/answer/list/sendAxios")
  @ResponseBody
  public List<Answer> listSendAxios(@RequestParam("reviewNo") Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) throws Exception {
    log.info("url ===========> /answer/list/sendAxios");
    log.info("reivewNo: " + reviewNo);
    log.info("startRow: " + startPage);
    List<Answer> answerList = answerService.selectAnswerList(reviewNo, AnswerStatus.Y, startPage, PAGE_ROW_COUNT);
    log.info("answerList: " + answerList);

    return answerList;
  }

  @PostMapping("/answer/add/sendAxios")
  @ResponseBody
  public Answer addSendAxios(Answer answer) throws Exception {
    log.info("url ===========> /answer/add/sendAxios");
    log.info("Answer: " + answer);

    answer.setMemberNo(1L); // 로그인 구현되면 Session에서 받아오도록 수정요망!

    Answer insertAnswer = answerService.addAnswer(answer);
    return insertAnswer;
  }

  @ResponseBody
  @PostMapping("/answer/update/sendAxios")
  public Answer updateSendAxios(Answer answer) throws Exception {
    log.info("url ===========> /answer/update/sendAxios");
    log.info("Answer: " + answer);
    Answer updateAnswer = answerService.updateAnswer(answer);
    return updateAnswer;
  }


  @ResponseBody
  @GetMapping("/answer/delete/sendAxios")
  public Answer deleteSendAxios(@RequestParam("answerNo") Long answerNo) throws Exception {
    log.info("url ===========> /delete/sendAxios");
    log.info("answerNo: " + answerNo);
    Answer deleteAnswer = answerService.deleteAnswer(answerNo);
    return deleteAnswer;
  }
}
