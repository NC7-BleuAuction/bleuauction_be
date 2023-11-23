package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {

  private final CreateJwt createJwt;
  private final AnswerService answerService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> answerList(@RequestHeader("Authorization") String authorizationHeader, Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/answer");
    log.info("reivewNo: {}", reviewNo);
    log.info("startPage: {}", startPage);

    createJwt.verifyAccessToken(authorizationHeader);
    return ResponseEntity.ok(answerService.selectAnswerList(reviewNo, startPage));
  }

  @PostMapping
  public ResponseEntity<Answer> answerAdd(@RequestHeader("Authorization") String authorizationHeader, Answer answer) {
    log.info("@PostMapping ===========> /api/answer");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("Answer: {}", answer);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    Answer insertAnswer = answerService.addAnswer(tokenMember, answer);

    return ResponseEntity.ok(insertAnswer);
  }

  @PutMapping
  public ResponseEntity<Answer> answerUpdate(@RequestHeader("Authorization") String authorizationHeader, Answer answer, Member member) throws Exception {
    log.info("@PutMapping ===========> /api/answer");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("answer: {}", answer);
    log.info("member: {}", member);

    createJwt.verifyAccessToken(authorizationHeader);
    TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
    return ResponseEntity.ok(answerService.updateAnswer(tokenMember, answer, member));
  }


  @DeleteMapping
  public ResponseEntity<Answer> answerDelete(@RequestHeader("Authorization") String authorizationHeader, Long answerNo, Long memberNo) throws Exception {
    log.info("@DeleteMapping ===========> /api/answer/delete");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("answerNo: {}", answerNo);
    log.info("memberNo: {}", memberNo);

      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      return ResponseEntity.ok(answerService.deleteAnswer(tokenMember, answerNo, memberNo));
  }
}
