package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.common.pagable.RowCountPerPage;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.exception.ReviewNotFoundException;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@ModuleService
@RequiredArgsConstructor
public class ReviewModuleService {

  private final AttachComponentService attachComponentService;
  private final ReviewRepository reviewRepository;
  private final AnswerRepository answerRepository;
  private final AttachRepository attachRepository;

  public List<Review> selectReviewList(Long storeNo, int startPage) {
    Pageable pageable = PageRequest.of(startPage, RowCountPerPage.REVIEW.getValue());
    List<Review> exitingReviewList = reviewRepository.findAllReviewsWithMembersByReviewStatus(storeNo, ReviewStatus.Y, pageable);
    for (Review review : exitingReviewList) {
      Optional<List<Attach>> optionalAttachList = Optional.ofNullable(attachRepository.findAllByReviewAndFileStatus(review, FileStatus.Y));
      if (optionalAttachList.isPresent()) {
        review.setReviewAttaches(optionalAttachList.get());
      }
    }
    return exitingReviewList;
  }

  public Review addReview(Review review, List<MultipartFile> multipartFiles) {
    review.setMember(SecurityUtils.getAuthenticatedUserToMember());
    Review insertReview = reviewRepository.save(review);
    if (!multipartFiles.isEmpty()) {
      ArrayList<Attach> attaches = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFiles) {
        if (multipartFile.getSize() > 0) {
          attachComponentService.saveWithReview(insertReview, FileUploadUsage.REVIEW, multipartFile);
        }
      }
      insertReview.setReviewAttaches(attaches);
    }
    return insertReview;
  }

  public Review updateReview(Review review) {

    Review exitingReview = reviewRepository.findById(review.getReviewNo())
                           .orElseThrow(() -> new ReviewNotFoundException(review.getReviewNo()));

    SecurityUtils.checkOwnsByMemberNo(exitingReview.getMember().getMemberNo());

    exitingReview.setReviewContent(review.getReviewContent());
    exitingReview.setReviewFreshness(review.getReviewFreshness());

    return reviewRepository.save(exitingReview);
  }


  public Review deleteReview(Long reviewNo) throws Exception {
    Review exitingReview = reviewRepository.findByReviewNoAndReviewStatus(reviewNo, ReviewStatus.Y)
                           .orElseThrow(() -> new ReviewNotFoundException(reviewNo));

    SecurityUtils.checkOwnsByMemberNo(exitingReview.getMember().getMemberNo());

    Optional<List<Attach>> optionalAttachList = Optional.ofNullable(attachRepository.findAllByReviewAndFileStatus(exitingReview, FileStatus.Y));
    if (optionalAttachList.isPresent()) {
      for (Attach exitingAttach : optionalAttachList.get()) {
        exitingAttach.setFileStatus(FileStatus.N);
      }
    }

    Optional<List<Answer>> optionalAnswerList = answerRepository.findAllByReviewNoAndAnswerStatus(exitingReview.getReviewNo(), AnswerStatus.Y);
    if (optionalAnswerList.isPresent()) {
      for (Answer exitingAnswer : optionalAnswerList.get()) {
        exitingAnswer.setAnswerStatus(AnswerStatus.N);
      }
    }
    exitingReview.setReviewStatus(ReviewStatus.N);
    return reviewRepository.save(exitingReview);
  }
}

