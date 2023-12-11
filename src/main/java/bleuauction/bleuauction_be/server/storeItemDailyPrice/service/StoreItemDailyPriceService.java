package bleuauction.bleuauction_be.server.storeItemDailyPrice.service;

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

  public List<StoreItemDailyPrice> selectSidpList() throws Exception {
    List<StoreItemDailyPrice>  exitingSidpList = storeItemDailyPriceRepository.findAllByDailyPriceStatus(DailyPriceStatus.Y)
                                                 .orElseThrow(() -> new Exception("존재하는 시세 데이터를 찾을 수 없습니다."));
    return exitingSidpList;
  }

  public StoreItemDailyPrice addStoreItemDailyPrice(StoreItemDailyPrice storeItemDailyPrice)  {
    return storeItemDailyPriceRepository.save(storeItemDailyPrice);
  }
}