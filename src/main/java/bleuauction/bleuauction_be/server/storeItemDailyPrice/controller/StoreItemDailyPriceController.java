package bleuauction.bleuauction_be.server.storeItemDailyPrice.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.service.StoreItemDailyPriceService;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.JwtConfig;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StoreItemDailyPriceController {
  private final CreateJwt createJwt;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;
  private final StoreItemDailyPriceService storeItemDailyPriceService;

  @GetMapping("/api/sidp/list")
  public ResponseEntity<?> reviewList(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
    log.info("url ===========> /api/sidp/list");

    try {
      // 토큰 검사
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult;
      }

      List<StoreItemDailyPrice> sidpList = storeItemDailyPriceService.selectSidpList(DailyPriceStatus.Y);
      log.info("storeItemDailyPriceList: " + sidpList);

      return ResponseEntity.ok(sidpList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createJwt.SERVER_ERROR);
    }
  }

  @PostMapping("/api/sidp/add")
  public ResponseEntity<?> storeItemDailyPriceAdd(@RequestHeader("Authorization") String authorizationHeader, @RequestBody StoreItemDailyPrice storeItemDailyPrice) throws Exception {
    log.info("url ===========> /api/sidp/add");
    log.info("StoreItemDailyPrice: " + storeItemDailyPrice);

    try {
      // 토큰 검사
      ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(authorizationHeader, createJwt);
      if (verificationResult != null) {
        return verificationResult; // 토큰 인증 실패
      }

      StoreItemDailyPrice insertStoreItemDailyPrice = storeItemDailyPriceService.addStoreItemDailyPrice(storeItemDailyPrice);
      log.info("insertStoreItemDailyPrice: " + insertStoreItemDailyPrice);
      return ResponseEntity.ok(insertStoreItemDailyPrice);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
