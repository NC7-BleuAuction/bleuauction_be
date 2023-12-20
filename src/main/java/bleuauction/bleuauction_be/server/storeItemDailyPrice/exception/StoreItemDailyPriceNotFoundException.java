package bleuauction.bleuauction_be.server.storeItemDailyPrice.exception;

import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;

public class StoreItemDailyPriceNotFoundException extends RuntimeException {

  private static final String DEFAULT_CONSTRUCTOR_MESSAGE = "[StoreItemDailyPriceNotFoundException] Not Found StoreItemDailyPrice";

  public StoreItemDailyPriceNotFoundException() {
    super();
  }

  public StoreItemDailyPriceNotFoundException(DailyPriceStatus dailyPriceStatus) {
    super(String.format(DEFAULT_CONSTRUCTOR_MESSAGE + ", Request dailyPriceNo No >>> %s", dailyPriceStatus));
  }

  public StoreItemDailyPriceNotFoundException(Long dailyPriceNo) {
    super(String.format(DEFAULT_CONSTRUCTOR_MESSAGE + ", Request dailyPriceNo No >>> %d", dailyPriceNo));
  }

}
