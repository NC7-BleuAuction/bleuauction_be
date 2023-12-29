package bleuauction.bleuauction_be.server.review.controller;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.service.ReviewComponentService;
import bleuauction.bleuauction_be.server.review.service.ReviewModuleService;
import bleuauction.bleuauction_be.server.store.service.StoreModuleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final StoreModuleService storeModuleService;
    private final ReviewComponentService reviewComponentService;
    private final AttachComponentService attachComponentService;

    @GetMapping
    public ResponseEntity<List<Review>> reviewList(
            @RequestParam(value = "storeNo") Long storeNo,
            @RequestParam(value = "startPage", defaultValue = "0") int startPage) {
        log.info("@GetMapping ===========> /api/reviews");
        log.info("storeNo: {}", storeNo);
        log.info("startPage: {}", startPage);
        List<Review> reviewList =
                reviewComponentService.findAllByStoreAndReviewStatus(
                        storeModuleService.findById(storeNo), ReviewStatus.Y, startPage);
        log.info("reviewList: {}", reviewList);

        return ResponseEntity.ok(reviewList);
    }

    @PostMapping
    public ResponseEntity<Review> reviewAdd(
            Review review,
            @RequestParam(name = "multipartFiles", required = false)
            List<MultipartFile> multipartFiles) {
        log.info("@PostMapping ===========> /api/review");
        log.info("Review: {}", review);
        log.info("MultipartFile: {}", multipartFiles);

        Review insertReview = reviewComponentService.addReview(review, multipartFiles);
        log.info("insertReview: " + insertReview);
        return ResponseEntity.ok(insertReview);
    }

    @PutMapping
    public ResponseEntity<Review> reviewUpdate(Review review) {
        log.info("@PutMapping ===========> /api/review");

        Review updateReview = reviewComponentService.updateReview(review);
        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping
    public ResponseEntity<Review> reviewDelete(Review review) {
        log.info("@DeleteMapping ===========> /api/review");
        log.info("reviewNo: {}", review);

        return ResponseEntity.ok(reviewComponentService.deleteReview(review));
    }

    @DeleteMapping("/attach")
    public ResponseEntity<Attach> reviewDeleteFile(Long fileNo) {
        log.info("@DeleteMapping ===========> api/review/attach");
        log.info("fileNo: {}", fileNo);
        return ResponseEntity.ok(attachComponentService.changeFileStatusDeleteByFileNo(fileNo));
    }
}
