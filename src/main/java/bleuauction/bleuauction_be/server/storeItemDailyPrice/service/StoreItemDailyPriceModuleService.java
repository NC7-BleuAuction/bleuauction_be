package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;

import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.exception.StoreItemDailyPriceNotFoundException;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ModuleService
@RequiredArgsConstructor
@Transactional
public class StoreItemDailyPriceModuleService {
  private final StoreItemDailyPriceRepository storeItemDailyPriceRepository;
  public List<StoreItemDailyPrice> findAllByDailyPriceStatus(DailyPriceStatus dailyPriceStatus) {
    return storeItemDailyPriceRepository.findAllByDailyPriceStatus(dailyPriceStatus)
                                                 .orElseThrow(() -> new StoreItemDailyPriceNotFoundException(dailyPriceStatus));
  }

  public StoreItemDailyPrice addStoreItemDailyPrice(StoreItemDailyPrice storeItemDailyPrice)  {
    return storeItemDailyPriceRepository.save(storeItemDailyPrice);
  }
}