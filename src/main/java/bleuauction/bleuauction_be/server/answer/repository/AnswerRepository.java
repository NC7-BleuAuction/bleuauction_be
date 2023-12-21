package bleuauction.bleuauction_be.server.answer.repository;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  Optional<Answer> findByIdAndStatus(Long id, AnswerStatus status);
  Page<Answer> findByReviewAndStatus(Review review, AnswerStatus status, Pageable pageable);
  Optional<Answer> findByIdAndReviewAndStatus(Long id, Review review, AnswerStatus status);
  List<Answer> findAllByReviewAndStatus(Review review, AnswerStatus status) ;
}
