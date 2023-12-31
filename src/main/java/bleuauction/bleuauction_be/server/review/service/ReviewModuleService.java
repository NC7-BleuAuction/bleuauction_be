package bleuauction.bleuauction_be.server.review.service;


import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.common.pagable.RowCountPerPage;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.exception.ReviewNotFoundException;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@Transactional
@ModuleService
@RequiredArgsConstructor
public class ReviewModuleService {

    private final ReviewRepository reviewRepository;

    public Review findByIdAndStatus(Review review) {
        return reviewRepository
                .findByIdAndStatus(review.getId(), ReviewStatus.Y)
                .orElseThrow(() -> new ReviewNotFoundException(review.getId()));
    }

    public List<Review> findAllByStoreAndReviewStatus(
            Store store, ReviewStatus status, Pageable pageable) {
        List<Review> exitingReviewList = reviewRepository.findAllByStoreAndStatusOrderByRegDatetimeDesc(store, status, pageable);
        return exitingReviewList;
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }
}
