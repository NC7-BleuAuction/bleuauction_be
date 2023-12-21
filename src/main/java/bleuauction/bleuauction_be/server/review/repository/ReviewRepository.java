package bleuauction.bleuauction_be.server.review.repository;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query("SELECT DISTINCT r " +
          "FROM Review r " +
          "JOIN FETCH r.member m " +
          "WHERE r.storeNo = :storeNo " +
          "AND r.reviewStatus = :reviewStatus " +
          "ORDER BY r.regDatetime DESC")
  List<Review> findAllReviewsWithMembersByReviewStatus(
          @Param("storeNo") Long storeNo,
          @Param("reviewStatus") ReviewStatus reviewStatus,
          Pageable pageable
  );

  Optional<Review> findByReviewNoAndReviewStatus(Long reviewNo, ReviewStatus reviewStatus);
}
