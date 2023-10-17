package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final AnswerRepository answerRepository;
  private final AttachRepository attachRepository;

  public List<Review> selectReviewList(Long storeNo, ReviewStatus reviewStatus, int startPage, int pageRowCount) {
    Pageable pageable = PageRequest.of(startPage, pageRowCount);
    List<Review>  exitingReviewList = reviewRepository.findAllReviewsWithMembersByReviewStatus(storeNo, reviewStatus, pageable);
    log.info("exitingReviewList:" +exitingReviewList.get(0).getMember());
    for (int i = 0; i < exitingReviewList.size(); i++) {
      List<Attach> exitingAttachList = attachRepository.findAllByReviewAndFileStatus(exitingReviewList.get(i), FileStatus.Y);
      exitingReviewList.get(i).setReviewAttaches(exitingAttachList);
    }
    return exitingReviewList;
  }

  public Review addReview(Review review) throws Exception {
    return reviewRepository.save(review);
  }

  public Review updateReview(Review review) throws Exception {
    Review exitingReview = reviewRepository.findById(review.getReviewNo()).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

    exitingReview.setReviewContent(review.getReviewContent());
    exitingReview.setReviewFreshness(review.getReviewFreshness());
    Review updateReview = reviewRepository.save(exitingReview);

    return updateReview;
  }


  public Review deleteReview(Long reviewNo) throws Exception {
    Review exitingReview = reviewRepository.findByReviewNoAndReviewStatus(reviewNo, ReviewStatus.Y).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

    List<Attach> exitingAttachList = attachRepository.findAllByReviewAndFileStatus(exitingReview, FileStatus.Y);
    if (exitingAttachList != null && exitingAttachList.size() > 0) {
      for (Attach exitingAttach : exitingAttachList) {
        exitingAttach.setFileStatus(FileStatus.N);
      }
    }

    List<Answer> exitingAnswerList = answerRepository.findAllByReviewNoAndAnswerStatus(exitingReview.getReviewNo(), AnswerStatus.Y);
    if (exitingAnswerList != null && exitingAnswerList.size() > 0) {
      for (Answer exitingAnswer : exitingAnswerList) {
        exitingAnswer.setAnswerStatus(AnswerStatus.N);
      }
    }
    exitingReview.setReviewStatus(ReviewStatus.N);

    Review deleteReview = reviewRepository.save(exitingReview);

    return deleteReview;
  }
}

