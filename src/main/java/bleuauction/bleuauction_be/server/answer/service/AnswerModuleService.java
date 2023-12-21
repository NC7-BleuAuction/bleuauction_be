package bleuauction.bleuauction_be.server.answer.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.exception.AnswerNotFoundException;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.common.exception.ForbiddenAccessException;
import bleuauction.bleuauction_be.server.common.pagable.RowCountPerPage;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@ModuleService
@RequiredArgsConstructor
public class AnswerModuleService {

  private final AnswerRepository answerRepository;

  public Map<String, Object> selectAnswerList(Long reviewNo, int startPage) {
    Pageable pageable = PageRequest.of(startPage, RowCountPerPage.ANSWER.getValue(), Sort.by(Sort.Order.desc("regDatetime")));
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

  public Answer addAnswer(Answer answer) {
    return answerRepository.save(answer);
  }

  public Answer updateAnswer(Answer answer, Member member) throws Exception {
    SecurityUtils.checkOwnsByMemberNo(member.getMemberNo());

    answer.setMember(member);
    Answer exitingAnswer = answerRepository.findByReviewNoAndAnswerNoAndAnswerStatus(answer.getReviewNo(), answer.getAnswerNo(), AnswerStatus.Y)
                          .orElseThrow(() -> new AnswerNotFoundException(answer.getReviewNo()));
    exitingAnswer.setAnswerContent(answer.getAnswerContent());
    Answer updateAnswer = answerRepository.save(exitingAnswer);
    return updateAnswer;
  }


  public Answer deleteAnswer(Long answerNo, Long memberNo) {
    SecurityUtils.checkOwnsByMemberNo(memberNo);

    Answer exitingAnswer = answerRepository.findByAnswerNoAndAnswerStatus(answerNo, AnswerStatus.Y)
                          .orElseThrow(() -> new AnswerNotFoundException(answerNo));
    exitingAnswer.setAnswerStatus(AnswerStatus.N);
    Answer deleteAnswer = answerRepository.save(exitingAnswer);
    return deleteAnswer;
  }

}
