package bleuauction.bleuauction_be.server.storeItemDailyPrice.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.service.StoreItemDailyPriceService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sidp")
public class StoreItemDailyPriceController {
  private final CreateJwt createJwt;
  private final StoreService storeService;
  private final StoreItemDailyPriceService storeItemDailyPriceService;

  @GetMapping
  public ResponseEntity<?> sidpList(@RequestHeader("Authorization") String authorizationHeader) {
    log.info("@GetMapping ===========> /api/sidp");

    try {
      createJwt.verifyAccessToken(authorizationHeader);

      List<StoreItemDailyPrice> sidpList = storeItemDailyPriceService.selectSidpList();
      log.info("storeItemDailyPriceList: {}", sidpList);

      return ResponseEntity.ok(sidpList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> sidpAdd(@RequestHeader("Authorization") String authorizationHeader,
                                   @RequestBody StoreItemDailyPrice storeItemDailyPrice) {
    log.info("@PostMapping ===========> /api/sidp");
    log.info("StoreItemDailyPrice: {}", storeItemDailyPrice);

    try {
      createJwt.verifyAccessToken(authorizationHeader);

      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      Member m = new Member();
      m.setMemberNo(tokenMember.getMemberNo());
      Store store = storeService.findStoresByMember(m);

      storeItemDailyPrice.setStoreNo(store.getStoreNo());
      StoreItemDailyPrice insertStoreItemDailyPrice = storeItemDailyPriceService.addStoreItemDailyPrice(storeItemDailyPrice);
      log.info("insertStoreItemDailyPrice: {} ", insertStoreItemDailyPrice);

      return ResponseEntity.ok(insertStoreItemDailyPrice);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
