package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

  @Autowired
  ReviewRepository reviewRepository;


  public Review addReview(Review review) {
    return reviewRepository.save(review);
  }

  public List<Review> selectReviewList() {
    return reviewRepository.findAllByReviewStatus(ReviewStatus.Y);
  }


  public String deleteReview(Long reviewNo) {
    Review exitingReview = reviewRepository.findById(reviewNo).orElse(null);
    if (exitingReview != null) {
      exitingReview.setReviewStatus(ReviewStatus.N);

      Review deleteReview = reviewRepository.save(exitingReview);

      if (deleteReview != null) {
        return "S"; // 업데이트 성공
      } else {
        return "F"; // 업데이트 실패
      }
    } else {
      return "N"; //못 찾음
    }
  }
}
