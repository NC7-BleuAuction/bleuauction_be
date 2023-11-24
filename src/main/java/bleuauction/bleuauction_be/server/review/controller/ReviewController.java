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
  public ResponseEntity<List<Review>> reviewList(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestParam(value = "storeNo") Long storeNo,
                                                 @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
    log.info("@GetMapping ===========> /api/reviews");
    log.info("storeNo: {}", storeNo);
    log.info("startPage: {}", startPage);
    createJwt.verifyAccessToken(authorizationHeader);
    List<Review> reviewList = reviewService.selectReviewList(storeNo, startPage);
    log.info("reviewList: {}", reviewList);

    return ResponseEntity.ok(reviewList);
  }

  @PostMapping
  public ResponseEntity<Review> reviewAdd(@RequestHeader("Authorization") String authorizationHeader, Review review, Member member,
                                          @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) throws Exception {
    log.info("@PostMapping ===========> /api/review");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("Review: {}", review);
    log.info("MultipartFile: {}", multipartFiles);


    createJwt.verifyAccessToken(authorizationHeader);

    review.setMember(member);
    Review insertReview = reviewService.addReview(review, multipartFiles);
    log.info("insertReview: " + insertReview);
    return ResponseEntity.ok(insertReview);
  }

  @PutMapping
  public ResponseEntity<Review> reviewUpdate(@RequestHeader("Authorization") String authorizationHeader, Review review) throws Exception {
    log.info("@PutMapping ===========> /api/review");

    createJwt.verifyAccessToken(authorizationHeader);
    Review updateReview = reviewService.updateReview(review);
    return ResponseEntity.ok(updateReview);
  }

  @DeleteMapping
  public ResponseEntity<Review> reviewDelete(@RequestHeader("Authorization") String authorizationHeader, Long reviewNo) throws Exception {
    log.info("@DeleteMapping ===========> /api/review");
    log.info("reviewNo: {}", reviewNo);
    createJwt.verifyAccessToken(authorizationHeader);
    return ResponseEntity.ok(reviewService.deleteReview(reviewNo));
  }

  @DeleteMapping("/attach")
  public ResponseEntity<Attach> reviewDeleteFile(@RequestHeader("Authorization") String authorizationHeader, Long fileNo) {
    log.info("@DeleteMapping ===========> api/review/attach");
    log.info("authorizationHeader: {}", authorizationHeader);
    log.info("fileNo: {}", fileNo);
    createJwt.verifyAccessToken(authorizationHeader);
    return ResponseEntity.ok(attachService.changeFileStatusToDeleteByFileNo(fileNo));
  }
}
