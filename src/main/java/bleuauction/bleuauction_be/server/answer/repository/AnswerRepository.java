package bleuauction.bleuauction_be.server.answer.repository;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  Optional<Answer> findByAnswerNoAndAnswerStatus(Long answerNo, AnswerStatus answerStatus);
  Page<Answer> findByReviewNoAndAnswerStatus(Long reviewNo, AnswerStatus answerStatus, Pageable pageable);
  Optional<Answer> findByReviewNoAndAnswerNoAndAnswerStatus(Long reviewNo, Long answerNo, AnswerStatus answerStatus) throws Exception;
  Optional<List<Answer>> findAllByReviewNoAndAnswerStatus(Long reviewNo, AnswerStatus answerStatus) throws Exception;
}
