package bleuauction.bleuauction_be.server.answer.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

  private final static int PAGE_ROW_COUNT = 2;
  private final AnswerRepository answerRepository;

  public Map<String, Object> selectAnswerList(Long reviewNo, int startPage) {
    Pageable pageable = PageRequest.of(startPage, PAGE_ROW_COUNT, Sort.by(Sort.Order.desc("regDatetime")));
    Page<Answer> page = answerRepository.findByReviewNoAndAnswerStatus(reviewNo,  AnswerStatus.Y, pageable);

    List<Answer> answerList = page.getContent();
    long totalRows = page.getTotalElements(); // 전체 행 수
    int totalPages = page.getTotalPages(); // 전체 페이지 수

    Map<String, Object> answerMap = new HashMap<>();
    answerMap.put("answerList", answerList);
    answerMap.put("totalRows", totalRows);
    answerMap.put("totalPages", totalPages);
    log.info("answerMap: ", answerMap);

    return answerMap;
  }

  public Answer addAnswer(TokenMember tokenMember, Answer answer) {
    Member m = new Member();
    m.setMemberNo(tokenMember.getMemberNo());
    answer.setMember(m);
    return answerRepository.save(answer);
  }

  public Answer updateAnswer(TokenMember tokenMember, Answer answer, Member member) throws Exception {
    if (tokenMember.getMemberNo() != member.getMemberNo()) {
      throw new Exception("답글 수정 권한이 없습니다!");
    }
    answer.setMember(member);
    Answer exitingAnswer = answerRepository.findByReviewNoAndAnswerNoAndAnswerStatus(answer.getReviewNo(), answer.getAnswerNo(), AnswerStatus.Y).orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));
    exitingAnswer.setAnswerContent(answer.getAnswerContent());
    Answer updateAnswer = answerRepository.save(exitingAnswer);
    return updateAnswer;
  }


  public Answer deleteAnswer(TokenMember tokenMember, Long answerNo, Long memberNo) throws Exception {
    if (tokenMember.getMemberNo() != memberNo) {
      throw new Exception("답글 삭제 권한이 없습니다!");
    }

    Answer exitingAnswer = answerRepository.findByAnswerNoAndAnswerStatus(answerNo, AnswerStatus.Y).orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));
    exitingAnswer.setAnswerStatus(AnswerStatus.N);
    Answer deleteAnswer = answerRepository.save(exitingAnswer);
    return deleteAnswer;
  }

}
