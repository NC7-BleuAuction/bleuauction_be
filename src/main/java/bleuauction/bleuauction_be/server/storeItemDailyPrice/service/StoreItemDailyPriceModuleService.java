package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.dto.StoreItemDailyPriceInsertRequest;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.exception.StoreItemDailyPriceNotFoundException;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ModuleService
@RequiredArgsConstructor
@Transactional
public class StoreItemDailyPriceModuleService {
    private final StoreItemDailyPriceRepository storeItemDailyPriceRepository;

    public List<StoreItemDailyPrice> findAllByDailyPriceStatus(DailyPriceStatus dailyPriceStatus) {
        return storeItemDailyPriceRepository.findAllByDailyPriceStatus(dailyPriceStatus);
    }

    public StoreItemDailyPrice addStoreItemDailyPrice(StoreItemDailyPrice storeItemDailyPrice) {
        return storeItemDailyPriceRepository.save(storeItemDailyPrice);
    }

    public StoreItemDailyPrice addStoreItemDailyPrice(
            StoreItemDailyPriceInsertRequest request, Store store) {
        return storeItemDailyPriceRepository.save(request.toEntity(store));
    }
}
