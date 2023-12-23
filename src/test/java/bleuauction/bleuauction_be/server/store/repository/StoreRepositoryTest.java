package bleuauction.bleuauction_be.server.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class StoreRepositoryTest {

    @Autowired private StoreRepository storeRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("가게 상태와 페이지 객체를 파라미터로 전달시, 페이징 여부 확인")
    void listTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));
        storeRepository.saveAll(
                List.of(
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름1", "111-11-11111"),
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름2", "111-11-11112"),
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름3", "111-11-11113"),
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름4", "111-11-11114"),
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름5", "111-11-11115")));

        // when
        Page<Store> foundList =
                storeRepository.findAllByStatus(StoreStatus.Y, PageRequest.of(0, 3));

        // then
        assertEquals(foundList.getSize(), 3);
        assertEquals(foundList.getContent().size(), 3);
    }

    @Test
    @DisplayName("Member객체르 파라미터로 전달할 때, 해당 Member의 가게정보가 존재하는 경우 Store객체를 Optional에 담아 반환한다.")
    void findByMemberNo_IsExistsReturnStoreTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));
        Store mockStore =
                storeRepository.save(
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름1", "111-11-11111"));

        // when
        Optional<Store> foundStore = storeRepository.findByMember(seller);

        // then
        assertTrue(foundStore.isPresent());
        assertEquals(foundStore.get(), mockStore);
    }

    @Test
    @DisplayName("Member객체르 파라미터로 전달할 때, 해당 Member의 가게정보가 존재하지 않는 경우 비어있는 Optional객체를 반환한다.")
    void findByMemberNo_IsNotExistsReturnEmptyOptionalTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));

        // when
        Optional<Store> foundStore = storeRepository.findByMember(seller);

        // then
        assertTrue(foundStore.isEmpty());
    }

    @Test
    @DisplayName("StoreName을 파라미터로 전달 할 때, 해당 StoreName의 가게정보가 존재하는 경우 Store객체를 Optional에 담아 반환한다.")
    void findByStoreName_IsExistsReturnStoreTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));
        Store mockStore =
                storeRepository.save(
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름1", "111-11-11111"));

        // when
        Optional<Store> foundStore = storeRepository.findByStoreName(mockStore.getStoreName());

        // then
        assertTrue(foundStore.isPresent());
        assertEquals(foundStore.get(), mockStore);
    }

    @Test
    @DisplayName("StoreName을 파라미터로 전달 할 때, 해당 StoreName의 가게정보가 존재하지 않는 경우 비어있는 Optional객체를 반환한다.")
    void findByStoreName_IsNotExistsReturnEmptyOptionaTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));

        // when
        Optional<Store> foundStore = storeRepository.findByStoreName("가게이름111");

        // then
        assertTrue(foundStore.isEmpty());
    }

    @Test
    @DisplayName("StoreName을 파라미터로 전달 할 때, 해당 StoreName의 가게정보가 존재하는 경우 True를 반환한")
    void existsStoreByStoreName_IsExistsReturnTrueTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));
        Store mockStore =
                storeRepository.save(
                        StoreUtilFactory.of(seller, "노량진수산시장", "가게이름1", "111-11-11111"));

        // when && then
        assertTrue(storeRepository.existsByStoreName(mockStore.getStoreName()));
    }

    @Test
    @DisplayName("StoreName을 파라미터로 전달 할 때, 해당 StoreName의 가게정보가 존재하지 않는 경우 False를 반환한")
    void existsStoreByStoreName_IsExistsReturnFalseTest() {
        // given
        Member seller =
                memberRepository.save(
                        MemberEntityFactory.of(
                                "test@test.com", "1111111", "테스트아무개", MemberCategory.S));

        // when && then
        assertFalse(storeRepository.existsByStoreName("가게이름111"));
    }
}
