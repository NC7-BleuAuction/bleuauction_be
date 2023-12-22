package bleuauction.bleuauction_be.server.answer.util;


import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;

public class AnswerEntityFactory {
    public static final Answer mockAnswer;

    static {
        Review review =
                Review.builder().reviewContent("테스트 리뷰 내용").reviewStatus(ReviewStatus.Y).build();
        review.setId(1L);
        Answer answer =
                Answer.builder()
                        .review(review)
                        .member(
                                MemberEntityFactory.of(
                                        "testCustomerMember1",
                                        "testCustomerMemberPwd",
                                        "테스트 일반 회원 이름",
                                        MemberCategory.M))
                        .content("테스트 답글 내용")
                        .status(AnswerStatus.Y)
                        .build();
        answer.setId(1L);

        mockAnswer = answer;
    }

    public static Answer of(Review review, Member member, String content, AnswerStatus status) {
        return Answer.builder()
                .review(review)
                .member(member)
                .content(content)
                .status(status)
                .build();
    }
}
