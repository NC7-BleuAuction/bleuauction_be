package bleuauction.bleuauction_be.server.store.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class StoreModuleService {
    private final StoreRepository storeRepository;

    /**
     * 가게의 상세정보를 조회 할때 사용되며, 가게가 존재하지 않는 경우 StoreNotFoundException가 발생한다
     *
     * @param storeNo 조회하고자 하는 가게의 Id
     * @return
     */
    public Store findById(Long storeNo) {
        return storeRepository
                .findById(storeNo)
                .orElseThrow(() -> new StoreNotFoundException(storeNo));
    }

    /**
     * Member Entity를 바탕으로 한 Store정보를 반환하며, 가게가 존재하지 않는 경우 StoreNotFoundException가 발생한다
     *
     * @param member 조회하고자 하는 가게의 판매자 Member객체
     * @return
     */
    public Store findByMember(Member member) {
        return storeRepository
                .findByMember(member)
                .orElseThrow(() -> new StoreNotFoundException(member));
    }

    /**
     * 가게의 상태에 맞춰서 가게정보를 반환한다
     *
     * @param storeStatus 조회하고자 하는 가게의 상태
     * @param pageable 조회하고자 하는 페이지 정보
     * @return
     */
    public Page<Store> findPageByStoreStatus(StoreStatus storeStatus, Pageable pageable) {
        return storeRepository.findAllByStatus(storeStatus, pageable);
    }

    /**
     * 가게 정보 수정 및 저장
     *
     * @param store 수정또는 저장하고자 하는 가게의 정보
     */
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    /**
     * 가게 정보 삭제
     *
     * @param storeNo 삭제하고자 하는 STORE의 ID
     */
    public void delete(Long storeNo) {
        storeRepository.deleteById(storeNo);
    }

    /**
     * 가게 이름 중복 검사
     *
     * @param storeName 중복 검사를 하고자 하는 가게 이름
     * @return
     */
    public boolean isExistByStoreName(String storeName) {
        return storeRepository.existsByStoreName(storeName);
    }
}
