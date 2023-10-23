package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
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
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final ReviewService reviewService;

  @GetMapping("/api/review/list")
  public ResponseEntity<?> reviewList(HttpSession session, @RequestParam(value = "storeNo", defaultValue = "1") Long storeNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) throws Exception{
    log.info("url ===========> /api/review/list");
    Optional<Member> loginUserOptional = Optional.ofNullable((Member) session.getAttribute("loginUser"));
    Member loginUser = loginUserOptional.orElseThrow(() -> new Exception("로그인 유저가 없습니다!"));
    log.info("loginUser: " + loginUser);

    log.info("storeNo: " + storeNo);
    log.info("startPage: " + startPage);
    try {
      Map<String, Object> responseMap = new HashMap<>();
      List<Review> reviewList = reviewService.selectReviewList(storeNo, ReviewStatus.Y, startPage, PAGE_ROW_COUNT);

      responseMap.put("loginUser", loginUser);
      responseMap.put("reviewList", reviewList);
      log.info("responseMap: " + responseMap);
      return ResponseEntity.ok(responseMap);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");

    }
  }


  @PostMapping("/api/review/add")
  public ResponseEntity<?> reviewAdd(HttpSession session, Review review, Member member,@RequestParam(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles) throws Exception {
    log.info("url ===========> /api/review/add");
    log.info("Review: " + review);
    log.info("MultipartFile: " + multipartFiles);

    try {
      Optional<Member> loginUserOptional = Optional.ofNullable((Member) session.getAttribute("loginUser"));
      Member loginUser = loginUserOptional.orElseThrow(() -> new Exception("로그인 유저가 없습니다!"));

      if (loginUser.getMemberNo() != member.getMemberNo()) {
        throw new Exception("리뷰 작성권한이 없습니다!");
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
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
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
      Attach deleteAttch = attachService.update(fileNo);
      return ResponseEntity.ok(deleteAttch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }
}
