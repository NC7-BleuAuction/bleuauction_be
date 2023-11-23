package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

  private final CreateJwt createJwt;
  private final AttachService attachService;
  private final ReviewService reviewService;

  @GetMapping
  public ResponseEntity<?> reviewList(@RequestHeader("Authorization") String authorizationHeader,
                                      @RequestParam(value = "storeNo") Long storeNo,
                                      @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/reviews");
    log.info("storeNo: {}", storeNo);
    log.info("startPage: {}", startPage);
    try {
      createJwt.verifyAccessToken(authorizationHeader);
      List<Review> reviewList = reviewService.selectReviewList(storeNo, startPage);
      log.info("reviewList: {}", reviewList);

      return ResponseEntity.ok(reviewList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> reviewAdd(@RequestHeader("Authorization") String authorizationHeader, Review review, Member member,
                                     @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {
    log.info("@PostMapping ===========> /api/review");
    log.info("authorizationHeader: {}",  authorizationHeader);
    log.info("Review: {}", review);
    log.info("MultipartFile: {}", multipartFiles);


    try {
      createJwt.verifyAccessToken(authorizationHeader);

      review.setMember(member);
      Review insertReview = reviewService.addReview(review, multipartFiles);
      log.info("insertReview: " + insertReview);

      return ResponseEntity.ok(insertReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PutMapping
  public ResponseEntity<?>  reviewUpdate(@RequestHeader("Authorization") String authorizationHeader, Review review) {
    log.info("@PutMapping ===========> /api/review");
    try {
      createJwt.verifyAccessToken(authorizationHeader);

      Review updateReview = reviewService.updateReview(review);
      return ResponseEntity.ok(updateReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping
  public ResponseEntity<?> reviewDelete(@RequestHeader("Authorization") String authorizationHeader, Long reviewNo) {
    log.info("@DeleteMapping ===========> /api/review");
    log.info("reviewNo: {}", reviewNo);
    try {
      createJwt.verifyAccessToken(authorizationHeader);

      Review deleteReview = reviewService.deleteReview(reviewNo);
      return ResponseEntity.ok(deleteReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping("/attach")
  public ResponseEntity<?> reviewDeleteFile(@RequestHeader("Authorization") String authorizationHeader, Long fileNo) {
    log.info("@DeleteMapping ===========> api/review/attach");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("fileNo: {}", fileNo);
    try {
      createJwt.verifyAccessToken(authorizationHeader);

      Attach deleteAttch = attachService.update(fileNo);
      return ResponseEntity.ok(deleteAttch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
