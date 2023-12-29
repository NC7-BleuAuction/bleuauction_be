package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;


import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreModuleService;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.dto.StoreItemDailyPriceInsertRequest;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@ComponentService
@RequiredArgsConstructor
public class StoreItemDailyPriceComponentService {
    private final SecurityUtils securityUtils;
    private final StoreModuleService storeModuleService;
    private final StoreItemDailyPriceModuleService sidpModuleService;

    public StoreItemDailyPrice findByIdAndDailyPriceStatus(Long sidpNo, DailyPriceStatus dailyPriceStatus) {
        return sidpModuleService.findByIdAndDailyPriceStatus(sidpNo, dailyPriceStatus);
    }

    public List<StoreItemDailyPrice> findAllByDailyPriceStatus(DailyPriceStatus dailyPriceStatus) {
        return sidpModuleService.findAllByDailyPriceStatus(dailyPriceStatus);
    }

    public StoreItemDailyPrice addStoreItemDailyPrice(StoreItemDailyPriceInsertRequest request) {
        StoreItemDailyPrice sidp = request.toEntity(storeModuleService.findById(request.getStoreId()));
        return sidpModuleService.save(sidp);
    }
}