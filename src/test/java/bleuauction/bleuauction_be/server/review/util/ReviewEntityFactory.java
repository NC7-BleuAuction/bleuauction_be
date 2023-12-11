package bleuauction.bleuauction_be.server.review.util;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewFreshness;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;

public class ReviewEntityFactory {
  public static final Review mockReview;

  static {
    Review review = Review.builder()
            .member(MemberEntityFactory.mockSellerMember)
            .storeNo(1L)
            .reviewContent("테스트 리뷰 내용")
            .reviewFreshness(ReviewFreshness.M)
            .build();
            review.setReviewNo(1L);

    mockReview = review;
  }



  public static Review of(Member member, Long storeNo, String reviewContent, ReviewFreshness reviewFreshness, ReviewStatus reviewStatus) {
    return Review.builder()
            .member(member)
            .storeNo(storeNo)
            .reviewContent(reviewContent)
            .reviewFreshness(reviewFreshness)
            .reviewStatus(reviewStatus)
            .build();
  }
}
