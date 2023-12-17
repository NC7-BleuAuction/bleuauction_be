package bleuauction.bleuauction_be.server.storeItemDailyPrice.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreComponentService;
import bleuauction.bleuauction_be.server.store.service.StoreModuleService;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.service.StoreItemDailyPriceService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sidp")
public class StoreItemDailyPriceController {
  private final CreateJwt createJwt;
  private final StoreModuleService storeModuleService;
  private final StoreItemDailyPriceService storeItemDailyPriceService;

  @GetMapping
  public ResponseEntity<List<StoreItemDailyPrice>> sidpList(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
    log.info("@GetMapping ===========> /api/sidp");

      createJwt.verifyAccessToken(authorizationHeader);
      List<StoreItemDailyPrice> sidpList = storeItemDailyPriceService.selectSidpList();
      log.info("storeItemDailyPriceList: {}", sidpList);
      return ResponseEntity.ok(sidpList);
  }

  @PostMapping
  public ResponseEntity<StoreItemDailyPrice> sidpAdd(@RequestHeader("Authorization") String authorizationHeader,
                                   @RequestBody StoreItemDailyPrice storeItemDailyPrice) throws Exception {
    log.info("@PostMapping ===========> /api/sidp");
    log.info("StoreItemDailyPrice: {}", storeItemDailyPrice);

      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      Member m = new Member();
      m.setMemberNo(tokenMember.getMemberNo());
      Store store = storeModuleService.findByMember(m);

      storeItemDailyPrice.setStoreNo(store.getStoreNo());
      StoreItemDailyPrice insertStoreItemDailyPrice = storeItemDailyPriceService.addStoreItemDailyPrice(storeItemDailyPrice);
      log.info("insertStoreItemDailyPrice: {} ", insertStoreItemDailyPrice);

      return ResponseEntity.ok(insertStoreItemDailyPrice);
  }
}
