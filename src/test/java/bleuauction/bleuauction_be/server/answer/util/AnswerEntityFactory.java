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
    Answer answer = Answer.builder()
            .reviewNo(1L)
            .member(MemberEntityFactory.of("testCustomerMember1", "testCustomerMemberPwd", "테스트 일반 회원 이름", MemberCategory.M ))
            .answerContent("테스트 답글 내용")
            .answerStatus(AnswerStatus.Y)
            .build();
    answer.setAnswerNo(1L);

    mockAnswer = answer;
  }


  public static Answer of(Long reviewNo, Member member, String answerContent, AnswerStatus answerStatus) {
    return Answer.builder()
            .reviewNo(reviewNo)
            .member(member)
            .answerContent(answerContent)
            .answerStatus(answerStatus)
            .build();
  }
}
