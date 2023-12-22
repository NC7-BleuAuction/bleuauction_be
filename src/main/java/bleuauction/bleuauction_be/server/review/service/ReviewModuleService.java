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

/**
 * TODO : 사실상 기존 로직과 다른게 뭔지 모르겠슴. 비즈니스 로직과 CRUD를 분리해서 주입안정성을 위해 분리한건데, 비즈니스 로직이 다들어가있슴. By.기현
 *
 * <p>TODO : 아니 Optional을 왜 이런식으로써여 !!!!!!!!1 아아아악 Optional<List<Attach>> optionalAttachList =
 * Optional.ofNullable(attachRepository.findAllByReviewAndFileStatus(exitingReview, FileStatus.Y))
 */
@Slf4j
@Transactional
@ModuleService
@RequiredArgsConstructor
public class ReviewModuleService {

    private final AttachComponentService attachComponentService;
    private final ReviewRepository reviewRepository;

    public List<Review> findAllByStoreAndReviewStatus(
            Store store, ReviewStatus status, int startPage) {
        Pageable pageable = PageRequest.of(startPage, RowCountPerPage.REVIEW.getValue());
        List<Review> exitingReviewList =
                reviewRepository.findAllByStoreAndStatusOrderByRegDatetimeDesc(
                        store, status, pageable);
        // TODO : 트랜잭션때문에 Attach에서 불러오도록 하신거같은데 굳이 호출할 필요 없었어요 By.기현
        exitingReviewList.forEach(
                review ->
                        review.getAttaches()
                                .forEach(
                                        attach ->
                                                log.info(
                                                        "ReviewModuleService.findAllByStoreAndReviewStatus attachId: {}, 존재함",
                                                        attach.getId())));
        return exitingReviewList;
    }

    /**
     * 리뷰 추가
     *
     * @param review
     * @param multipartFiles
     * @return
     */
    public Review addReview(Review review, List<MultipartFile> multipartFiles) {
        review.setMember(SecurityUtils.getAuthenticatedUserToMember());
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.forEach(
                    multipartFile -> {
                        if (multipartFile.getSize() > 0) {
                            attachComponentService.saveWithReview(
                                    review, FileUploadUsage.REVIEW, multipartFile);
                        }
                    });
        }
        return reviewRepository.save(review);
    }

    public Review updateReview(Review review) {
        Review exitingReview =
                reviewRepository
                        .findById(review.getId())
                        .orElseThrow(() -> new ReviewNotFoundException(review.getId()));

        SecurityUtils.checkOwnsByMemberNo(exitingReview.getMember().getId());

        exitingReview.setContent(review.getContent());
        exitingReview.setFreshness(review.getFreshness());

        return reviewRepository.save(exitingReview);
    }

    public Review deleteReview(Long reviewNo) throws Exception {
        Review exitingReview =
                reviewRepository
                        .findByIdAndStatus(reviewNo, ReviewStatus.Y)
                        .orElseThrow(() -> new ReviewNotFoundException(reviewNo));

        SecurityUtils.checkOwnsByMemberNo(exitingReview.getMember().getId());
        exitingReview.deleteReview();
        return exitingReview;
    }
}
