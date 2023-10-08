package bleuauction.bleuauction_be.server.review.repository;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findAllByReviewStatus(ReviewStatus reviewStatus);
}
