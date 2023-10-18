package bleuauction.bleuauction_be.server.answer.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Slf4j
@Service
public class AnswerService {

  @Autowired
  AnswerRepository answerRepository;

  public Answer addAnswer(Answer answer) throws Exception {
    return answerRepository.save(answer);
  }
  public Page<Answer> selectAnswerList(Long reviewNo, AnswerStatus answerStatus, int startPage, int pageRowCount) throws Exception {
    Pageable pageable = PageRequest.of(startPage, pageRowCount, Sort.by(Sort.Order.desc("regDatetime")));
    Page<Answer> page = answerRepository.findByReviewNoAndAnswerStatus(reviewNo, answerStatus, pageable);

    return page;
  }

  public Answer updateAnswer(Answer answer) throws Exception {
    Answer exitingAnswer = answerRepository.findByReviewNoAndAnswerNoAndAnswerStatus(answer.getReviewNo(), answer.getAnswerNo(), AnswerStatus.Y).orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));
    exitingAnswer.setAnswerContent(answer.getAnswerContent());
    Answer updateAnswer = answerRepository.save(exitingAnswer);

    return updateAnswer;
  }


  public Answer deleteAnswer(Long answerNo) throws Exception {
    Answer exitingAnswer = answerRepository.findById(answerNo).orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));

    exitingAnswer.setMdfDatetime(new Timestamp(System.currentTimeMillis()));
    exitingAnswer.setAnswerStatus(AnswerStatus.N);
    Answer deleteAnswer = answerRepository.save(exitingAnswer);

    return deleteAnswer;
  }

}
