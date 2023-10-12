package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReviewService {

  @Autowired
  ReviewRepository reviewRepository;

  public List<Review> selectReviewList(Long storeNo, ReviewStatus reviewStatus, int startPage, int pageRowCount) {
    Pageable pageable = PageRequest.of(startPage, pageRowCount);
    return reviewRepository.findAllByReviewStatus(storeNo, reviewStatus, pageable);
  }

  public Review addReview(Review review) throws Exception {
    return reviewRepository.save(review);
  }

  public Review updateReview(Review review) throws Exception {
    Review exitingReview = reviewRepository.findById(review.getReviewNo()).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

    exitingReview.setReviewContent(review.getReviewContent());
    exitingReview.setReviewFreshness(review.getReviewFreshness());
    exitingReview.setMdfDatetime(new Timestamp(System.currentTimeMillis()));
    Review updateReview = reviewRepository.save(exitingReview);

    return updateReview;
  }


  public Review deleteReview(Long reviewNo) throws Exception {
    Review exitingReview = reviewRepository.findById(reviewNo).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

    exitingReview.setMdfDatetime(new Timestamp(System.currentTimeMillis()));
    exitingReview.setReviewStatus(ReviewStatus.N);
    Review deleteReview = reviewRepository.save(exitingReview);

    return deleteReview;
  }
}

