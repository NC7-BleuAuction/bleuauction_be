package bleuauction.bleuauction_be.server.review.util;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewFreshness;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;

import static bleuauction.bleuauction_be.server.review.entity.ReviewFreshness.M;

public class ReviewEntityFactory {
    public static final Review mockReview;

    static {
        Store mockStore =  Store.builder()
                .marketName("노량진 쑤산시장")
                .storeName("블루오크션")
                .licenseNo("111-111-111111")
                .build();
        mockStore.setId(1L);


        Review review =
                Review.builder()
                        .member(MemberEntityFactory.mockSellerMember)
                        .store(mockStore)
                        .content("테스트 리뷰 내용")
                        .freshness(M)
                        .build();
        review.setId(1L);

        mockReview = review;
    }

    public static Review of(
            Member member,
            Store store,
            String content,
            ReviewFreshness freshness,
            ReviewStatus status) {
        return Review.builder()
                .member(member)
                .store(store)
                .content(content)
                .freshness(freshness)
                .status(status)
                .build();
    }
}
