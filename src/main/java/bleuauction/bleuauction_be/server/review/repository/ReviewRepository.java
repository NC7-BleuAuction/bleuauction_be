package bleuauction.bleuauction_be.server.review.repository;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query("SELECT a FROM Review a " +
          "WHERE a.storeNo = :storeNo " +
          "AND a.reviewStatus = :reviewStatus " +
          "ORDER BY a.regDatetime DESC")
  List<Review> findAllByReviewStatus(
          @Param("storeNo") Long storeNo,
          @Param("reviewStatus") ReviewStatus reviewStatus,
          Pageable pageable
  );

  Optional<Review> findByReviewNoAndReviewStatus(Long reviewNo, ReviewStatus reviewStatus);

}
