package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

  @GetMapping("/review/list/sendAxios")
  public List<Review> listAxios(@RequestParam(value = "storeNo", defaultValue = "1") Long storeNo, @RequestParam(value = "startPage", defaultValue = "0") int startPage) {

    log.info("/review/list/sendAxios");
    log.info("storeNo: " + storeNo);
    log.info("startPage: " + startPage);

    List<Review> reviewList = reviewService.selectReviewList(storeNo, ReviewStatus.Y, startPage, PAGE_ROW_COUNT);
    log.info("reviewList: " + reviewList);
    log.info("reviewList.size(): " + reviewList.size());

    return reviewList;
  }

  @PostMapping("/review/add/sendAxios")
  public ResponseEntity<?> addSendAxios(Review review, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) throws Exception {
    log.info("url ===========> /add/sendAxios");
    log.info("Review: " + review);
    log.info("MultipartFile: " + multipartFiles);

    review.setMemberNo(1L); // 로그인 구현되면 Session에서 받아오도록 수정요망!

    try {
      Review insertReview = reviewService.addReview(review);
      log.info("insertReview: " + insertReview);

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "review/", multipartFile);
        attach.setReviewNo(insertReview);
            attaches.add(attach);
          }
        }
        log.info("attaches: " + attaches);
        List<Attach> insertAttaches = attachService.addAttachs(attaches);
        log.info("insertAttaches: " + insertAttaches);
      }
      return ResponseEntity.ok(insertReview);
    } catch (Exception e) {
      return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }
  }

  @PostMapping("/review/update/sendAxios")
  public Review updateSendAxios(Review review) throws Exception {
    log.info("url ===========> /update/sendAxios");
    log.info("Review: " + review);
    Review updateReview = reviewService.updateReview(review);
    return updateReview;
  }

  @GetMapping("/review/delete/sendAxios")
  public Review deleteSendAxios(@RequestParam("reviewNo") Long reviewNo) throws Exception {
    log.info("url ===========> /delete/sendAxios");
    log.info("reviewNo: " + reviewNo);
    Review deleteReview = reviewService.deleteReview(reviewNo);
    return deleteReview;
  }
}
