package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreUpdateUnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreComponentService {

    private final StoreModuleService storeModuleService;
    private final AttachComponentService attachComponentService;

    /**
     * Store를 조회요청한 갯수만큼 가져가길 희망한 기능
     *
     * @param storeStatus
     * @param page
     * @param limit
     * @return
     */
    public List<Store> selectStoreList(StoreStatus storeStatus, int page, int limit) {
        Page<Store> findResult = storeModuleService.findPageByStoreStatus(storeStatus, PageRequest.of(page, limit));
        log.info("[{}] FindStoreList By StoreStatus, FindResult >>> {}", this.getClass().getName(), findResult);
        return findResult.getContent();
    }

    /**
     * 가게 등록
     *
     * @param request
     */
    public void signup(StoreSignUpRequest request, Member member) {
        if (!MemberCategory.isMemberSeller(member.getMemberCategory())) {
            throw new IllegalArgumentException("판매자 회원만 등록 가능합니다.");
        }
        validateDuplicateStore(request.getStoreName());
        storeModuleService.save(request.getStoreEntity(member));
    }

    /**
     * Store 정보 수정
     *
     * @param loginUser          로그인된 판매자 사용자
     * @param updateStoreRequest 수정하고자 하는 가게 정보
     */
    public void updateStore(Member loginUser, UpdateStoreRequest updateStoreRequest) {
        // JWT토큰을 기반으로 한 로그인 사용자의 마켓정보 획득
        Store updateStore = storeModuleService.findByMember(loginUser);

        // 첨부 파일 목록 추가
        if (updateStoreRequest.getProfileImage() != null && updateStoreRequest.getProfileImage().getSize() > 0) {
            // 첨부 파일 저장 및 결과를 insertAttaches에 할당 및 Attach정보에 대해서는 Store객체에 추가
            attachComponentService.saveWithStore(updateStore, FileUploadUsage.STORE, updateStoreRequest.getProfileImage());
        }

        log.info("[{}] Store Info Update Start, Store [ID : {}, Name : {}]",
                this.getClass().getName(), updateStore.getId(), updateStoreRequest.getStoreName());
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

        storeModuleService.save(updateStore);
        log.info("[{}] Store Info Update End, Store [ID : {}, Name : {}]",
                this.getClass().getName(), updateStore.getId(), updateStore.getStoreName());
    }

    /**
     * 가게 폐업처리
     *
     * @param storeNo 폐업처리하고자 하는 가게의 ID
     * @param member  폐업처리를 요청한 사용자
     */
    public void withDrawStore(Long storeNo, Member member) {
        Store store = storeModuleService.findById(storeNo);

        // 가게 소유자가 아닌 경우
        if (store.getMember() == null || !store.getMember().getId().equals(member.getId())) {
            log.error("올바른 가게 정보가 아닙니다.");
            throw new StoreUpdateUnAuthorizedException(store, member);
        }
        // 가게 상태를 'N'으로 변경하여 탈퇴 처리
        store.changeStoreStatusN();
        storeModuleService.save(store);
        log.info("가게가 성공적으로 폐업되었습니다. 가게번호: {}, 가게명: {}", member.getId(), store.getStoreName());
    }

    /**
     * 매개변수로 받은 가게명이 중복으로 사용중인지를 검증
     *
     * @param storeName
     */
    private void validateDuplicateStore(String storeName) {
        if (storeModuleService.isExistByStoreName(storeName)) {
            throw new IllegalStateException("이미 존재하는 가게 입니다.");
        }
    }
}