package bleuauction.bleuauction_be.server.review.service;

import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewFreshness;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import bleuauction.bleuauction_be.server.review.util.ReviewEntityFactory;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock private ReviewRepository reviewRepository;

    @Mock private AttachComponentService attachComponentService;
    @InjectMocks private ReviewModuleService reviewModuleService;

    private final Long TEST_MEMBER_NO = 1L;
    private final Long TEST_STORE_NO = 1L;
    private final Long TEST_REVIEW_NO = 1L;
    private final String TEST_REVIEW_CONTENT = "테스트 리뷰 내용";
    private final String TEST_REVIEW_FRESHNESS = "M";

    private final String TEST_MARKETNAME = "노량진 쑤산시장";
    private final String TEST_STORENAME = "블루오크션";
    private final String TEST_LICENSE = "111-111-111111";

    @Test
    @DisplayName(
            "testSelectReviewList(): 리뷰 리스트를 조회시 StoreNo와 startPage를 파라미터로 제공할 때, 해당 가게에 대한 리뷰 리스트를 반환한다.")
    void testSelectReviewList() {
        // given
        final Long TEST_STORE_NO = 1L;
        final int TEST_START_PAGE = 0;
        final int PAGE_ROW_COUNT = 4;

        Store mockStore = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        mockStore.setId(TEST_STORE_NO);

        List<Review> mockReviewList =
                List.of(
                        ReviewEntityFactory.of(
                                MemberEntityFactory.mockSellerMember,
                                mockStore,
                                "테스트 리뷰 내용1",
                                ReviewFreshness.L,
                                ReviewStatus.Y),
                        ReviewEntityFactory.of(
                                MemberEntityFactory.mockSellerMember,
                                mockStore,
                                "테스트 리뷰 내용2",
                                ReviewFreshness.M,
                                ReviewStatus.Y),
                        ReviewEntityFactory.of(
                                MemberEntityFactory.mockSellerMember,
                                mockStore,
                                "테스트 리뷰 내용3",
                                ReviewFreshness.H,
                                ReviewStatus.Y),
                        ReviewEntityFactory.of(
                                MemberEntityFactory.mockSellerMember,
                                mockStore,
                                "테스트 리뷰 내용4",
                                ReviewFreshness.L,
                                ReviewStatus.Y));
        Pageable pageable = PageRequest.of(TEST_START_PAGE, PAGE_ROW_COUNT);
        given(reviewRepository.findAllByStoreAndStatusOrderByRegDatetimeDesc(mockStore, ReviewStatus.Y, pageable))
                .willReturn(mockReviewList);

        // when
        List<Review> selectReviewList =
                reviewModuleService.findAllByStoreAndReviewStatus(mockStore, ReviewStatus.Y, TEST_START_PAGE);

        // then
        assertEquals(mockReviewList, selectReviewList);
    }

    @Test
    @DisplayName("testAddReview(): 리뷰 등록에 성공한다. [첨부파일 미첨부]")
    void testAddReview() throws Exception {
        // given
        Store mockStore = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        mockStore.setId(TEST_STORE_NO);

        List<MultipartFile> mockFileList = new ArrayList<>();

        Review mockReview =
                ReviewEntityFactory.of(
                        MemberEntityFactory.mockSellerMember,
                        mockStore,
                        TEST_REVIEW_CONTENT,
                        ReviewFreshness.M,
                        ReviewStatus.Y);

        given(reviewRepository.save(mockReview))
                .willAnswer(
                        invocation -> {
                            Review param = invocation.getArgument(0);
                            param.setId(TEST_REVIEW_NO);
                            return param;
                        });

        // when
        Review addReview = reviewModuleService.addReview(mockReview, mockFileList);

        // then
        assertNotNull(addReview);

        assertEquals(TEST_REVIEW_NO, addReview.getId());
        assertEquals(TEST_MEMBER_NO, addReview.getMember().getId());
        assertEquals(TEST_STORE_NO, addReview.getStore().getId());
        assertEquals(mockStore, addReview.getStore());
        assertEquals(TEST_REVIEW_CONTENT, addReview.getContent());
        assertEquals(ReviewFreshness.M, addReview.getFreshness());
        assertEquals(ReviewStatus.Y, mockReview.getStatus());
    }

    @Test
    @DisplayName("testUpdateReview(): 리뷰 수정에 성공한다.")
    void testUpdateReview() throws Exception {
        // given
        Store mockStore = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        mockStore.setId(TEST_STORE_NO);

        Review mockOriginReview =
                ReviewEntityFactory.of(
                        MemberEntityFactory.mockSellerMember,
                        mockStore,
                        TEST_REVIEW_CONTENT,
                        ReviewFreshness.M,
                        ReviewStatus.Y);
        mockOriginReview.setId(TEST_REVIEW_NO);

        given(reviewRepository.findById(mockOriginReview.getId()))
                .willReturn(Optional.of(mockOriginReview));

        mockOriginReview.setContent(TEST_REVIEW_CONTENT + "수정");
        mockOriginReview.setFreshness(ReviewFreshness.H);

        given(reviewRepository.save(mockOriginReview)).willReturn(mockOriginReview);

        // when
        Review updateReview = reviewModuleService.updateReview(mockOriginReview);

        // then
        assertEquals(mockOriginReview, updateReview);
    }

    @Test
    @DisplayName("testDeleteReview(): 리뷰를 삭제 상태로 변경한다.")
    void testDeleteReview() throws Exception {
        // given
        Store mockStore = StoreUtilFactory.of(TEST_MARKETNAME, TEST_STORENAME, TEST_LICENSE);
        mockStore.setId(TEST_STORE_NO);

        Review mockReview =
                ReviewEntityFactory.of(
                        MemberEntityFactory.mockSellerMember,
                        mockStore,
                        TEST_REVIEW_CONTENT,
                        ReviewFreshness.M,
                        ReviewStatus.Y);
        mockReview.setId(TEST_REVIEW_NO);
        given(reviewRepository.findByIdAndStatus(mockReview.getId(), mockReview.getStatus()))
                .willReturn(Optional.of(mockReview));
        given(reviewRepository.save(mockReview)).willAnswer(invocation -> {
            Review param = invocation.getArgument(0);
            param.setStatus(ReviewStatus.N);
            param.deleteReview();
            return param;
        });

        // when
        Review deleteReview = reviewModuleService.deleteReview(mockReview.getId());

        // then
        assertEquals(mockReview, deleteReview);
    }
}
