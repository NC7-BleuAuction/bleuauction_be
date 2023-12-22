package bleuauction.bleuauction_be.server.review.repository;


import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStoreAndStatusOrderByRegDatetimeDesc(Store store, ReviewStatus status, Pageable pageable);
    Optional<Review> findByIdAndStatus(Long id, ReviewStatus status);
}
