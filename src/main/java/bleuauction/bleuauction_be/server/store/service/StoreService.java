package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {


    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public List<Store> selectStoreList(StoreStatus storeStatus, int startPage, int pageRowCount) {
        Pageable pageable = PageRequest.of(startPage, pageRowCount);
        List<Store>  exitingStoreList = storeRepository.findAllByStoreStatus(storeStatus, pageable);
        return exitingStoreList;
    }

    // 가게등록
    public Store signup(StoreSignUpRequest request, Long memberNo)
            throws NotFoundException, IllegalAccessException {
        Member member = memberRepository.findById(memberNo).orElseThrow(
                MemberNotFoundException::new);
        if (request.getMemberCategory() != MemberCategory.S) {
    throw new IllegalAccessException("판매자 회원만 등록 가능합니다.");
        }
        Store store = request.getStoreEntity(member); // builder로 바꿔보세요
        store.setWeekdayStartTime(request.getWeekdayStartTime());
        store.setWeekdayEndTime(request.getWeekdayEndTime());
        store.setWeekendStartTime(request.getWeekendStartTime());
        store.setWeekendEndTime(request.getWeekendEndTime());
        validateDuplicateStore(store);
        return storeRepository.save(store);
    }

    // 가게이름 중복검사
    private void validateDuplicateStore(Store store) {
        Optional<Store> findStores = storeRepository.findBystoreName(store.getStoreName());
        if (findStores.isPresent()) {
            throw new IllegalStateException("이미 존재하는 가게 입니다.");
        }
    }
    // 가게 정보 수정
    public void update(Store store) {
        storeRepository.save(store);
    }
    // 가게 정보 삭제
    public void delete(Long storeNo) {
        storeRepository.deleteById(storeNo);
    }

    public Optional<Store> selectStore(Long storeNo) {
        return storeRepository.findById(storeNo);
    }
}