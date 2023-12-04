package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

  @Value("${ncp.bucket.name}")
  private String bucketName;
  @Value("${ncp.bucket.paths.review}")
  private String bucketPath;
  private static final int PAGE_ROW_COUNT = 4;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachComponentService attachComponentService;
  private final ReviewRepository reviewRepository;
  private final AnswerRepository answerRepository;
  private final AttachRepository attachRepository;

  public List<Review> selectReviewList(Long storeNo, int startPage) {
    Pageable pageable = PageRequest.of(startPage, PAGE_ROW_COUNT);
    List<Review> exitingReviewList = reviewRepository.findAllReviewsWithMembersByReviewStatus(storeNo, ReviewStatus.Y, pageable);
    for (int i = 0; i < exitingReviewList.size(); i++) {
      Optional<List<Attach>> optionalAttachList = Optional.ofNullable(attachRepository.findAllByReviewAndFileStatus(exitingReviewList.get(i), FileStatus.Y));
      if (optionalAttachList.isPresent()) {
        exitingReviewList.get(i).setReviewAttaches(optionalAttachList.get());
      }
    }
    return exitingReviewList;
  }

  public Review addReview(Review review, List<MultipartFile> multipartFiles) throws Exception {


    Review insertReview = reviewRepository.save(review);
    if (!multipartFiles.isEmpty()) {
      ArrayList<Attach> attaches = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFiles) {
        if (multipartFile.getSize() > 0) {
          Attach attach = ncpObjectStorageService.uploadFile(new Attach(), bucketName, bucketPath, multipartFile);
          attach.setReview(insertReview);
          attaches.add(attach);
        }
      }
      insertReview.setReviewAttaches(attaches);
    }
    return insertReview;
  }

  public Review updateReview(Review review) throws Exception {
    Review exitingReview = reviewRepository.findById(review.getReviewNo()).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

    exitingReview.setReviewContent(review.getReviewContent());
    exitingReview.setReviewFreshness(review.getReviewFreshness());

    return reviewRepository.save(exitingReview);
  }


  public Review deleteReview(Long reviewNo) throws Exception {
    Review exitingReview = reviewRepository.findByReviewNoAndReviewStatus(reviewNo, ReviewStatus.Y).orElseThrow(() -> new Exception("해당 리뷰가 존재하지 않습니다!"));

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

