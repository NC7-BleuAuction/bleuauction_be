package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.POST;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final CreateJwt createJwt;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final ReviewService reviewService;

  @GetMapping("/api/reviews")
  public ResponseEntity<?> review(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "storeNo") Long storeNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/reviews");
    log.info("storeNo: " + storeNo);
    log.info("startPage: " + startPage);
    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }
      List<Review> reviewList = reviewService.selectReviewList(storeNo, ReviewStatus.Y, startPage);
      log.info("reviewList: " + reviewList);

      return ResponseEntity.ok(reviewList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @PostMapping("/api/review")
  public ResponseEntity<?> reviewAdd(@RequestHeader("Authorization") String authorizationHeader, Review review, Member member,
                                     @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {
    log.info("@PostMapping ===========> /api/review");
    log.info("authorizationHeader: " +  authorizationHeader);
    log.info("Review: " + review);
    log.info("MultipartFile: " + multipartFiles);


    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      review.setMember(member);
      Optional<Review> insertReview = Optional.ofNullable(reviewService.addReview(review).orElseThrow(() -> new Exception("리뷰 등록 실패!")));
      log.info("insertReview: " + insertReview);

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(), "bleuauction-bucket", "review/", multipartFile);
            attach.setReview(insertReview.get());
            attaches.add(attach);
          }
        }
        log.info("attaches: " + attaches);
        List<Attach> insertAttaches = attachService.addAttachs(attaches);
        log.info("insertAttaches: " + insertAttaches);
      }
      return ResponseEntity.ok(insertReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PutMapping("/api/review")
  public ResponseEntity<?>  reviewUpdate(@RequestHeader("Authorization") String authorizationHeader, Review review) {
    log.info("PutMapping ===========> /api/review");
    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      Optional<Review> updateReview = Optional.ofNullable(reviewService.updateReview(review).orElseThrow(() -> new Exception("리뷰 수정 실패!")));
      return ResponseEntity.ok(updateReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @DeleteMapping("/api/review")
  public ResponseEntity<?> reviewDelete(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("reviewNo") Long reviewNo) {
    log.info("@DeleteMapping ===========> /api/review");
    log.info("reviewNo: " + reviewNo);
    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      Optional<Review> deleteReview = Optional.ofNullable(reviewService.deleteReview(reviewNo).orElseThrow(() -> new Exception("리뷰 삭제 실패!")));
      return ResponseEntity.ok(deleteReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @DeleteMapping("/api/review/attach")
  public ResponseEntity<?> reviewDeleteFile(@RequestHeader("Authorization") String authorizationHeader, Long fileNo) {
    log.info("@DeleteMapping ===========> api/review/attach");
    log.info("authorizationHeader: " + authorizationHeader);
    log.info("fileNo: " + fileNo);
    try {
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      Attach deleteAttch = attachService.update(fileNo);
      return ResponseEntity.ok(deleteAttch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }
}
