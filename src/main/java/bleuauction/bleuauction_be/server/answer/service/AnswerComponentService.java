package bleuauction.bleuauction_be.server.answer.service;


import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.common.pagable.RowCountPerPage;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.service.ReviewModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Transactional
@ComponentService
@RequiredArgsConstructor
public class AnswerComponentService {
  private final SecurityUtils securityUtils;
  private final AnswerModuleService answerModuleService;
  private final ReviewModuleService reviewModuleService;

  @Transactional(readOnly = true)
  public Map<String, Object> selectAnswerList(Long reviewNo, int startPage) {
    Pageable pageable =
            PageRequest.of(
                    startPage,
                    RowCountPerPage.ANSWER.getValue(),
                    Sort.by(Sort.Order.desc("regDatetime")));

    Page<Answer> page = answerModuleService.findAllByReviewAndStatus(reviewNo, AnswerStatus.Y, pageable);

    Map<String, Object> answerMap = Map.of("answerList", page.getContent(),
            "totalRows", page.getTotalElements(), // 전체 행 수
            "totalPages", page.getTotalPages()); // 전체 페이지 수

    log.info("answerMap: ", answerMap);
    return answerMap;
  }

  public Answer addAnswer(Answer answer) {
    return answerModuleService.addAnswer(answer);
  }

  public Answer updateAnswer(Answer answer, Member member) {
    securityUtils.checkOwnsByMemberNo(member.getId());
    answer.setMember(member);
    Answer exitingAnswer =
            answerModuleService
                    .findByIdAndReviewAndStatus(
                            answer.getId(), reviewModuleService.findByIdAndStatus(answer.getReview()), AnswerStatus.Y);
    exitingAnswer.setContent(answer.getContent());
    return answerModuleService.updateAnswer(exitingAnswer);
  }

  public Answer deleteAnswer(Answer answer, Member member) {
    securityUtils.checkOwnsByMemberNo(member.getId());
    Answer exitingAnswer =
            answerModuleService
                    .findByIdAndReviewAndStatus(
                            answer.getId(), reviewModuleService.findByIdAndStatus(answer.getReview()), AnswerStatus.Y);
    exitingAnswer.setStatus(AnswerStatus.N);
    return answerModuleService.deleteAnswer(exitingAnswer);
  }
}