package bleuauction.bleuauction_be.server.review.service;


import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.common.pagable.RowCountPerPage;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional
@ComponentService
@RequiredArgsConstructor
public class ReviewComponentService {

  private final SecurityUtils securityUtils;
  private final AttachComponentService attachComponentService;
  private final ReviewModuleService reviewModuleService;

  /**
   * 리뷰 등록
   *
   * @param review
   * @param multipartFiles
   * @return
   */
  public Review addReview(Review review, List<MultipartFile> multipartFiles) {
    review.setMember(securityUtils.getAuthenticatedUserToMember());
    if (multipartFiles != null && !multipartFiles.isEmpty()) {
      multipartFiles.forEach(
              multipartFile -> {
                if (multipartFile.getSize() > 0) {
                  attachComponentService.saveWithReview(
                          review, FileUploadUsage.REVIEW, multipartFile);
                }
              });
    }
    return reviewModuleService.addReview(review);
  }

  /**
   * 리뷰 리스트 조회
   *
   * @param store
   * @param status
   * @param startPage
   * @return
   */
  public List<Review> findAllByStoreAndReviewStatus(
          Store store, ReviewStatus status, int startPage) {
    Pageable pageable = PageRequest.of(startPage, RowCountPerPage.REVIEW.getValue());
    List<Review> exitingReviewList = reviewModuleService.findAllByStoreAndReviewStatus(store, status, pageable);
    return exitingReviewList;
  }

  /**
   * 리뷰 수정
   *
   * @param review
   * @return
   */
  public Review updateReview(Review review) {
    Review exitingReview = reviewModuleService.findByIdAndStatus(review);
    securityUtils.checkOwnsByMemberNo(exitingReview.getMember().getId());
    exitingReview.setContent(review.getContent());
    exitingReview.setFreshness(review.getFreshness());
    return reviewModuleService.updateReview(exitingReview);
  }

  /**
   * 리뷰 삭제
   *
   * @param review
   * @return
   */
  public Review deleteReview(Review review) {
    Review exitingReview = reviewModuleService.findByIdAndStatus(review);
    securityUtils.checkOwnsByMemberNo(exitingReview.getMember().getId());
    exitingReview.deleteReview();
    return exitingReview;
  }
}
