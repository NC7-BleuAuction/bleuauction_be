package bleuauction.bleuauction_be.server.answer.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.service.AnswerService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
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
  public ResponseEntity<?> answerList(@RequestHeader("Authorization") String authorizationHeader, Long reviewNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/answer");
    log.info("reivewNo: {}", reviewNo);
    log.info("startPage: {}", startPage);

    try {
      createJwt.verifyAccessToken(authorizationHeader);
      Map<String, Object> answerMap = answerService.selectAnswerList(reviewNo, startPage);
      return ResponseEntity.ok(answerMap);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> answerAdd(@RequestHeader("Authorization") String authorizationHeader, Answer answer) {
    log.info("@PostMapping ===========> /api/answer");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("Answer: {}", answer);

    try {
      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      Answer insertAnswer = answerService.addAnswer(tokenMember, answer);

      return ResponseEntity.ok(insertAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PutMapping
  public ResponseEntity<?> answerUpdate(@RequestHeader("Authorization") String authorizationHeader, Answer answer, Member member) {
    log.info("@PutMapping ===========> /api/answer");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("answer: {}", answer);
    log.info("member: {}", member);

    try {
      createJwt.verifyAccessToken(authorizationHeader);

      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);
      Answer updateAnswer = answerService.updateAnswer(tokenMember, answer, member);
      return ResponseEntity.ok(updateAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }

  }


  @DeleteMapping
  public ResponseEntity<?> answerDelete(@RequestHeader("Authorization") String authorizationHeader, Long answerNo,  Long memberNo) {
    log.info("@DeleteMapping ===========> /api/answer/delete");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("answerNo: {}", answerNo);
    log.info("memberNo: {}", memberNo);

    try {
      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      Answer deleteAnswer = answerService.deleteAnswer(tokenMember, answerNo, memberNo);
      return ResponseEntity.ok(deleteAnswer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }
}
