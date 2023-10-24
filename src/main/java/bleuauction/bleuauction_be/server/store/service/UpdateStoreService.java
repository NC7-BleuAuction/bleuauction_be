package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateStoreService {

    private final StoreRepository storeRepository;

    public void updateStore(Long storeNo, UpdateStoreRequest updateStoreRequest,
            MultipartFile profileImage)
            throws StoreNotFoundException {
        Optional<Store> optionalStore = storeRepository.findById(storeNo);

        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            store.setStoreName(updateStoreRequest.getStoreName());
            store.setMarketName(updateStoreRequest.getMarketName());
            store.setLicenseNo(updateStoreRequest.getLicenseNo());
            store.setStoreZipcode(updateStoreRequest.getStoreZipcode());
            store.setStoreAddr(updateStoreRequest.getStoreAddr());
            store.setStoreDetailAddr(updateStoreRequest.getStoreDetailAddr());
            store.setWeekdayStartTime(updateStoreRequest.getWeekdayStartTime());
            store.setWeekdayEndTime(updateStoreRequest.getWeekdayEndTime());
            store.setWeekendStartTime(updateStoreRequest.getWeekendStartTime());
            store.setWeekendEndTime(updateStoreRequest.getWeekendEndTime());
            store.setStoreAttaches((List<Attach>) updateStoreRequest.getProfileImage());

            storeRepository.save(store);
        } else {
            throw new StoreNotFoundException("가게를 찾을 수 없습니다.");
        }
    }
}
