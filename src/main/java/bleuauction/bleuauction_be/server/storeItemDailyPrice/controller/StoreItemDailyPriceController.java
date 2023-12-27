package bleuauction.bleuauction_be.server.storeItemDailyPrice.controller;


import bleuauction.bleuauction_be.server.storeItemDailyPrice.dto.StoreItemDailyPriceInsertRequest;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.service.StoreItemDailyPriceComponentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sidp")
public class StoreItemDailyPriceController {

    private final StoreItemDailyPriceComponentService sidpComponentService;

    @GetMapping
    public ResponseEntity<List<StoreItemDailyPrice>> sidpList() {
        List<StoreItemDailyPrice> sidpList =
                sidpComponentService.findAllByDailyPriceStatus(DailyPriceStatus.Y);
        log.info("storeItemDailyPriceList: {}", sidpList);
        return ResponseEntity.ok(sidpList);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('S')")
    public ResponseEntity<StoreItemDailyPrice> sidpAdd(
            @RequestBody StoreItemDailyPriceInsertRequest request) {
        log.info("@PostMapping ===========> /api/sidp");
        log.info("StoreItemDailyPriceInsertRequest: {}", request);

        StoreItemDailyPrice insertedSidp = sidpComponentService.addStoreItemDailyPrice(request);

        log.info("insertedSidp: {} ", insertedSidp);

        return ResponseEntity.ok(insertedSidp);
    }
}
