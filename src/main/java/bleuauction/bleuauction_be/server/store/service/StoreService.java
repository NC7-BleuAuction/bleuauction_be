package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 가게의 상세정보를 조회 할때 사용되며, 가게가 존재하지 않는 경우 StoreNotFoundException가 발생한다
     *
     * @param storeNo
     * @return
     */
    public Store findStoreById(Long storeNo) {
        return storeRepository.findById(storeNo).orElseThrow(() -> new StoreNotFoundException(storeNo));
    }

    /**
     * Member Entity를 바탕으로 한 Store정보를 반환하며, 가게가 존재하지 않는 경우 StoreNotFoundException가 발생한다
     *
     * @param requeestMember
     * @return
     */
    public Store findStoreByMember(Member requeestMember) {
        return storeRepository.findByMemberNo(requeestMember)
                .orElseThrow(() -> new StoreNotFoundException(requeestMember.getMemberNo()));
    }


    /**
     * Store를 조회요청한 갯수만큼 가져가길 희망한 기능
     * [TODO] : 현재 Page는 정상적으로 작동이 안되고 있을 확률이 높음. Page객체만 가져가게 변경이 필요는 함.
     *
     * @param storeStatus
     * @param page
     * @param limit
     * @return
     */
    public List<Store> selectStoreList(StoreStatus storeStatus, int page, int limit) {
        Page<Store> findResult = storeRepository.findAllByStoreStatus(storeStatus, PageRequest.of(page, limit));
        log.info("[{}] FindStoreList By StoreStatus, FindResult >>> {}", this.getClass().getName(), findResult);

        return findResult.getContent();
    }

    /**
     * 가게 등록
     * @param request
     * @throws IllegalAccessException
     * @throws IllegalStateException
     */
    public void signup(StoreSignUpRequest request, Member member) throws IllegalAccessException, IllegalStateException {
        if (member.getMemberCategory() != MemberCategory.S) {
            throw new IllegalAccessException("판매자 회원만 등록 가능합니다.");
        }
        validateDuplicateStore(request.getStoreName());
        storeRepository.save(request.getStoreEntity(member));
    }

    /**
     * 가게 정보 수정
     * @param store
     */
    public void update(Store store) {
        storeRepository.save(store);
    }

    /**
     * 가게 정보 삭제
     * @param storeNo
     */
    public void delete(Long storeNo) {
        storeRepository.deleteById(storeNo);
    }

    public Optional<Store> selectStore(Long storeNo) {
        return storeRepository.findById(storeNo);
    }

    /**
     * Store 정보 수정
     * @param updateStore
     * @param updateStoreRequest
     * @throws StoreNotFoundException
     */
    public void updateStore(Store updateStore, UpdateStoreRequest updateStoreRequest)throws StoreNotFoundException {
        log.info("[{}] Store Info Update Start, Store [ID : {}, Name : {}]",
                this.getClass().getName(), updateStore.getStoreNo(), updateStoreRequest.getStoreName());
        updateStore.setStoreName(updateStoreRequest.getStoreName());
        updateStore.setMarketName(updateStoreRequest.getMarketName());
        updateStore.setLicenseNo(updateStoreRequest.getLicenseNo());
        updateStore.setStoreZipcode(updateStoreRequest.getStoreZipcode());
        updateStore.setStoreAddr(updateStoreRequest.getStoreAddr());
        updateStore.setStoreDetailAddr(updateStoreRequest.getStoreDetailAddr());
        updateStore.setWeekdayStartTime(updateStoreRequest.getWeekdayStartTime());
        updateStore.setWeekdayEndTime(updateStoreRequest.getWeekdayEndTime());
        updateStore.setWeekendStartTime(updateStoreRequest.getWeekendStartTime());
        updateStore.setWeekendEndTime(updateStoreRequest.getWeekendEndTime());

        storeRepository.save(updateStore);
        log.info("[{}] Store Info Update End, Store [ID : {}, Name : {}]",
                this.getClass().getName(), updateStore.getStoreNo(), updateStore.getStoreName());
    }

    public void withDrawStore(Store store, Member member){
        // 가게 소유자가 아닌 경우
        if (!store.getMemberNo().getMemberNo().equals(member.getMemberNo())) {
            log.error("올바른 가게 정보가 아닙니다.");
            throw new StoreUpdateUnAuthorizedException(store, member);
        }
        // 가게 상태를 'N'으로 변경하여 탈퇴 처리
        store.setStoreStatus(StoreStatus.N);
        this.update(store);
        log.info("가게가 성공적으로 폐업되었습니다. 가게번호: {}, 가게명: {}", member.getMemberNo(), store.getStoreName());
    }

    /**
     * 매개변수로 받은 가게명이 중복으로 사용중인지를 검증
     * @param storeName
     */
    private void validateDuplicateStore(String storeName) {
        if (storeRepository.existsStoreByStoreName(storeName)) {
            throw new IllegalStateException("이미 존재하는 가게 입니다.");
        }
    }

//    public Optional<Store> findStoresByMember(Member m) {
//        return storeRepository.findByMemberNo(m);
//    }

    public Store findStoresByMember(Member m) throws Exception {
        return storeRepository.findByMemberNo(m).orElseThrow(() -> new Exception("등록된 가게가 존재하지 않는 회원입니다!"));
    }
}