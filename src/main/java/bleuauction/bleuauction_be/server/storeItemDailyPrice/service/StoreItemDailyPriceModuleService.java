package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.dto.StoreItemDailyPriceInsertRequest;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ModuleService
@RequiredArgsConstructor
public class StoreItemDailyPriceModuleService {
    private final StoreItemDailyPriceRepository sidpRepository;

    public StoreItemDailyPrice findByIdAndDailyPriceStatus(Long sidpNo, DailyPriceStatus dailyPriceStatus) {
        return sidpRepository.findByIdAndDailyPriceStatus(sidpNo, dailyPriceStatus);
    }

    public List<StoreItemDailyPrice> findAllByDailyPriceStatus(DailyPriceStatus dailyPriceStatus) {
        return sidpRepository.findAllByDailyPriceStatus(dailyPriceStatus);
    }

    public StoreItemDailyPrice save(StoreItemDailyPrice sidp) {
        return sidpRepository.save(sidp);
    }

}