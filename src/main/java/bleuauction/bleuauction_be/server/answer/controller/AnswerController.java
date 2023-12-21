package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.service.AnswerModuleService;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {

  private final AnswerModuleService answerModuleService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> answerList(Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/answer");
    log.info("reivewNo: {}", reviewNo);
    log.info("startPage: {}", startPage);

    return ResponseEntity.ok(answerModuleService.selectAnswerList(reviewNo, startPage));
  }

  @PostMapping
  public ResponseEntity<Answer> answerAdd(Answer answer) {
    log.info("@PostMapping ===========> /api/answer");
    log.info("Answer: {}", answer);

    answer.setMember(SecurityUtils.getAuthenticatedUserToMember());
    Answer insertAnswer = answerModuleService.addAnswer(answer);
    return ResponseEntity.ok(insertAnswer);
  }

  @PutMapping
  public ResponseEntity<Answer> answerUpdate(Answer answer, Member member) throws Exception {
    log.info("@PutMapping ===========> /api/answer");
    log.info("answer: {}", answer);
    log.info("member: {}", member);

    return ResponseEntity.ok(answerModuleService.updateAnswer(answer, member));
  }


  @DeleteMapping
  public ResponseEntity<Answer> answerDelete(Long answerNo, Long memberNo) throws Exception {
    log.info("@DeleteMapping ===========> /api/answer/delete");
    log.info("answerNo: {}", answerNo);
    log.info("memberNo: {}", memberNo);

      return ResponseEntity.ok(answerModuleService.deleteAnswer(answerNo, memberNo));
  }
}
