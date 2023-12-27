package bleuauction.bleuauction_be.server.answer.service;


import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.exception.AnswerNotFoundException;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@ModuleService
@RequiredArgsConstructor
public class AnswerModuleService {

    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;

    public Answer findByIdAndReviewAndStatus(Long answerNo, Review review, AnswerStatus answerStatus) {
        return answerRepository.findByIdAndReviewAndStatus(answerNo, review, answerStatus).orElseThrow(() -> new AnswerNotFoundException("해당 답글이 존재하지 않습니다!"));
    }

    @Transactional(readOnly = true)
    public Page<Answer> findAllByReviewAndStatus(Long reviewNo, AnswerStatus answerStatus, Pageable pageable) {
        return answerRepository.findAllByReviewAndStatus(findReviewById(reviewNo), answerStatus, pageable);
    }

    public Answer addAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Answer updateAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Answer deleteAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다!"));
    }
}
