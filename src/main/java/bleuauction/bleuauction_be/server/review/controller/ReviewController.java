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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

  final int PAGE_ROW_COUNT = 4;
  private final CreateJwt createJwt;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final ReviewService reviewService;

  @GetMapping("/api/review/list")
  public ResponseEntity<?> reviewList(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "storeNo") Long storeNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) throws Exception{
    log.info("url ===========> /api/review/list");

    log.info("storeNo: " + storeNo);
    log.info("startPage: " + startPage);

    try {
      // 토큰 검사
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }
      List<Review> reviewList = reviewService.selectReviewList(storeNo, ReviewStatus.Y, startPage, PAGE_ROW_COUNT);
      return ResponseEntity.ok(reviewList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");

    }
  }

  @PostMapping("/api/review/add")
  public ResponseEntity<?> reviewAdd(@RequestHeader("Authorization") String authorizationHeader, Review review, Member member,
          @RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) throws Exception {
    log.info("url ===========> /api/review/add");
    log.info("authorizationHeader: " +  authorizationHeader);
    log.info("Review: " + review);
    log.info("MultipartFile: " + multipartFiles);


    try {
      // 토큰 검사
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      review.setMember(member);
      Review insertReview = reviewService.addReview(review);
      log.info("insertReview: " + insertReview);

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(), "bleuauction-bucket", "review/", multipartFile);
            attach.setReview(insertReview);
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

  @PostMapping("/api/review/update")
  public ResponseEntity<?>  reviewUpdate(Review review) throws Exception {
    log.info("url ===========> /api/review/update");

    try {
      Review updateReview = reviewService.updateReview(review);
      return ResponseEntity.ok(updateReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @GetMapping("/api/review/delete")
  public ResponseEntity<?> reviewDelete(@RequestParam("reviewNo") Long reviewNo) throws Exception {
    log.info("url ===========> /api/review/delete");
    log.info("reviewNo: " + reviewNo);

    try {
      Review deleteReview = reviewService.deleteReview(reviewNo);
      return ResponseEntity.ok(deleteReview);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @GetMapping("/api/review/deleteFile")
  public ResponseEntity<?> reviewDeleteFile(@RequestParam Long fileNo) throws Exception {
    log.info("url ===========> /api/review/deleteFile");
    log.info("fileNo: " + fileNo);
    try {
      Attach deleteAttch = attachService.changeFileStatusToDeleteByFileNo(fileNo);
      return ResponseEntity.ok(deleteAttch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }
}