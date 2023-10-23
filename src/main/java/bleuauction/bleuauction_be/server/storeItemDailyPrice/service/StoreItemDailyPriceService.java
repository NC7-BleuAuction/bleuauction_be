package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;

import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.repository.StoreItemDailyPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreItemDailyPriceService {


  private final StoreItemDailyPriceRepository storeItemDailyPriceRepository;

  public List<StoreItemDailyPrice> selectSidpList(DailyPriceStatus dailyPriceStatus) {
    List<StoreItemDailyPrice>  exitingStoreItemDailyPriceList = storeItemDailyPriceRepository.findAllByDailyPriceStatus(dailyPriceStatus);
    return exitingStoreItemDailyPriceList;
  }

  public StoreItemDailyPrice addStoreItemDailyPrice(StoreItemDailyPrice storeItemDailyPrice) throws Exception {
    return storeItemDailyPriceRepository.save(storeItemDailyPrice);
  }


}