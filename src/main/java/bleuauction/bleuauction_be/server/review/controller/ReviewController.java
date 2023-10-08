package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  ReviewService reviewService;

  @PostMapping("/add")
  @ResponseBody
  public String addReivew(Review review) {

    log.info("/review/add");
    log.info("review: " + review);

    review.setMemberNo(1L); // 로그인 구현되면 Session에서 받아오도록 수정요망!

    Review insertReview = reviewService.addReview(review);

    Gson gson = new Gson();
    Map<String, Object> map = new HashMap<>();

    if (insertReview != null) {
      map.put("status", "success");
    } else {
      map.put("status", "fail");
    }
    return gson.toJson(map);
  }


  @GetMapping("/list")
  public String list(Model model) {

    log.info("/review/list");

    List<Review> reviewList = reviewService.selectReviewList();

    int reviewSubListCnt = 2;

    List<Review> reviewSubList = null;
    if (reviewList.size() >= reviewSubListCnt) {
      reviewSubList = reviewList.subList(0, reviewSubListCnt);
    } else {
      reviewSubList = reviewList.subList(0, reviewList.size());
    }



    log.info("reviewList: " + reviewList);
    log.info("reviewSubList: " + reviewSubList);

    model.addAttribute("reviewListSize", reviewList.size());
    model.addAttribute("reviewSubList", reviewSubList);

    return "testReviewList";
  }

  @ResponseBody
  @GetMapping("/list/sendAjax")
  public String listSendAjax(@RequestParam(name = "reviewLength", defaultValue = "0") int reviewLength) {

    log.info("/list/sendAjax");
    log.info("reviewLength: " + reviewLength);

    Gson gson = new Gson();
    List<Review> reviewList = reviewService.selectReviewList();
    log.info("storeList: " + reviewList);
    log.info("storeList.size(): " + reviewList.size());

    List<Review> reviewSubList = null;
    if (reviewLength > reviewList.size()) {
      reviewSubList = reviewList.subList(reviewLength - 2, reviewList.size());
    } else {
      reviewSubList = reviewList.subList(reviewLength - 2, reviewLength);
    }

    log.info("reviewSubList: " + reviewSubList);

    Map<String, Object> map = new HashMap<>();
    map.put("reviewSubList", reviewSubList);

    return gson.toJson(map);
  }

  @ResponseBody
  @GetMapping("/delete/sendAjax")
  public String deleteSendAjax(@RequestParam("reviewNo") Long reviewNo) {

    log.info("/delete/sendAjax");
    log.info("reviewNo: " + reviewNo);

    Gson gson = new Gson();
    Map<String, Object> map = new HashMap<>();

    String statusStr = reviewService.deleteReview(reviewNo);

    if("S".equals(statusStr)) {
      map.put("status", "success");
    } else if("F".equals(statusStr)) {
      map.put("status", "fail");
    } else {
      map.put("status", "not");
    }
    return gson.toJson(map);
  }
}
