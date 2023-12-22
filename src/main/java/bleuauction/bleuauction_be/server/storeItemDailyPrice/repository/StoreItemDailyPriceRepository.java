package bleuauction.bleuauction_be.server.storeItemDailyPrice.repository;


import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.DailyPriceStatus;
import bleuauction.bleuauction_be.server.storeItemDailyPrice.entity.StoreItemDailyPrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreItemDailyPriceRepository extends JpaRepository<StoreItemDailyPrice, Long> {
    Optional<List<StoreItemDailyPrice>> findAllByDailyPriceStatus(
            DailyPriceStatus dailyPriceStatus);
}
