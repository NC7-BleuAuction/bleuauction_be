package bleuauction.bleuauction_be.server.answer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.answer.util.AnswerEntityFactory;
import bleuauction.bleuauction_be.server.common.utils.SecurityUtils;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WithMockUser
class AnswerServiceTest {

    @Mock private AnswerRepository answerRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private SecurityUtils securityUtils;

    @Mock private AnswerComponentService answerComponentService;

    @InjectMocks private AnswerModuleService answerModuleService;

    private final Long TEST_MEMBER_NO = 1L;
    private final Long TEST_REVIEW_NO = 1L;

    private final Long TEST_ANSWER_NO = 1L;
    private final String TEST_ANSWER_CONTENT = "테스트 답글 내용";
    private final String TEST_MAIL = "test@test.com";
    private final String TEST_PWD = "testpassword123!";
    private final String TEST_NAME = "테스트 이름";

    private final Sort SORT_BY_ORDER_DESC = Sort.by(Sort.Order.desc("regDatetime"));

    @Test
    @DisplayName(
            "testSelectAnswerList(): 답글 리스트를 조회시 reviewNo와 startPage를 파라미터로 제공할 때, 해당 reviewNo와 startPage에 맞는 페이지에 해당하는 답글 리스트와 해당 페이징에 대한 정보를 갖는 Map을 반환한다.")
    void testSelectAnswerList() {
        // given
        final int TEST_START_PAGE = 0;
        final int PAGE_ROW_COUNT = 2;

        Review mockTestReview = Review.builder().build();
        mockTestReview.setId(TEST_REVIEW_NO);

        List<Answer> mockAnswerList =
                List.of(
                        AnswerEntityFactory.of(
                                mockTestReview,
                                MemberEntityFactory.of(
                                        TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M),
                                TEST_ANSWER_CONTENT + 1,
                                AnswerStatus.Y),
                        AnswerEntityFactory.of(
                                mockTestReview,
                                MemberEntityFactory.of(
                                        TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M),
                                TEST_ANSWER_CONTENT + 2,
                                AnswerStatus.Y),
                        AnswerEntityFactory.of(
                                mockTestReview,
                                MemberEntityFactory.of(
                                        TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M),
                                TEST_ANSWER_CONTENT + 3,
                                AnswerStatus.Y),
                        AnswerEntityFactory.of(
                                mockTestReview,
                                MemberEntityFactory.of(
                                        TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M),
                                TEST_ANSWER_CONTENT + 4,
                                AnswerStatus.Y));

        Pageable pageable = PageRequest.of(TEST_START_PAGE, PAGE_ROW_COUNT, SORT_BY_ORDER_DESC);
        Page<Answer> mockPage = new PageImpl<>(mockAnswerList, pageable, 3L);
        given(answerRepository.findAllByReviewAndStatus(mockTestReview, AnswerStatus.Y, pageable))
                .willReturn(mockPage);
        given(reviewRepository.findById(TEST_REVIEW_NO)).willReturn(Optional.of(mockTestReview));

        List<Answer> answerList = mockPage.getContent();
        long totalRows = mockPage.getTotalElements(); // 전체 행 수
        int totalPages = mockPage.getTotalPages(); // 전체 페이지 수

        Map<String, Object> mockAnswerMap = new HashMap<>();
        mockAnswerMap.put("answerList", answerList);
        mockAnswerMap.put("totalRows", totalRows);
        mockAnswerMap.put("totalPages", totalPages);

        // when
        Map<String, Object> selectAnswerMap =
                answerComponentService.selectAnswerList(TEST_REVIEW_NO, TEST_START_PAGE);

        // then
        assertEquals(mockAnswerMap, selectAnswerMap);
    }

    @Test
    @DisplayName("testAddAnswer(): Answer객체와 TokenMember객체가 주어질 경우 답글 등록에 성공한다.")
    void testAddAnswer() {
        // given
        Member mockMember =
                Member.builder()
                        .email(TEST_MAIL)
                        .name(TEST_NAME)
                        .category(MemberCategory.M)
                        .build();
        mockMember.setId(TEST_MEMBER_NO);

        Review mockTestReview = Review.builder().build();
        mockTestReview.setId(TEST_REVIEW_NO);

        Answer mockAnswer =
                AnswerEntityFactory.of(
                        mockTestReview,
                        MemberEntityFactory.of(
                                TEST_MAIL, TEST_PWD, TEST_NAME, mockMember.getCategory()),
                        TEST_NAME,
                        AnswerStatus.Y);

        given(answerRepository.save(mockAnswer)).willReturn(mockAnswer);
        Answer addAnswer = answerModuleService.addAnswer(mockAnswer);

        // then
        assertEquals(mockAnswer, addAnswer);
    }

    @Test
    @DisplayName(
            "testUpdateAnswer(): TokenMember, Answer, Member 객체 등이 주어질 때 TokenMember.memberNo와 Memebr.memberNo가 같을 때 AnswerStatus.Y에 해당하는 답글 수정에 성공한다.")
    @WithMockUser(
            username = TEST_MAIL,
            password = "sample",
            roles = {"M"})
    void testUpdateAnswer() throws Exception {
        // given
        Review mockTestReview = Review.builder().build();
        mockTestReview.setId(TEST_REVIEW_NO);

        Member mockMember =
                Member.builder()
                        .email(TEST_MAIL)
                        .name(TEST_NAME)
                        .category(MemberCategory.M)
                        .build();
        mockMember.setId(TEST_MEMBER_NO);

        Member mockCustomerMember =
                MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, mockMember.getCategory());
        mockCustomerMember.setId(TEST_MEMBER_NO);

        Answer mockAnswer =
                AnswerEntityFactory.of(
                        mockTestReview, mockCustomerMember, TEST_NAME, AnswerStatus.Y);
        given(
                answerRepository.findByIdAndReviewAndStatus(
                        mockAnswer.getId(), mockAnswer.getReview(), AnswerStatus.Y))
                .willReturn(Optional.of(mockAnswer));
        mockAnswer.setContent(TEST_ANSWER_CONTENT + "수정");
        given(answerRepository.save(mockAnswer)).willReturn(mockAnswer);

        // when
        Answer updateAnswer = answerComponentService.updateAnswer(mockAnswer, mockCustomerMember);

        // then
        assertEquals(mockAnswer, updateAnswer);
    }

    @Test
    @DisplayName(
            "testDeleteAnswer(): TokenMember, answerNo, memebrNo 등의 파라미터가 주어질 때 TokenMemebr.memebrNo와 memebrNo가 같고 AnswerStatus.Y에 해당하는 답글의 상태를 AnswerStatus.N 으로 변경한다.")
    void testDeleteAnswer() throws Exception {
        // given
        Review mockTestReview = Review.builder().build();
        mockTestReview.setId(TEST_REVIEW_NO);

        Member mockMember =
                Member.builder()
                        .email(TEST_MAIL)
                        .name(TEST_NAME)
                        .category(MemberCategory.M)
                        .build();
        mockMember.setId(TEST_MEMBER_NO);

        Member mockCustomerMember =
                MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, mockMember.getCategory());
        mockCustomerMember.setId(TEST_MEMBER_NO);

        Answer mockAnswer =
                AnswerEntityFactory.of(
                        mockTestReview, mockCustomerMember, TEST_NAME, AnswerStatus.Y);

        given(answerRepository.findByIdAndStatus(TEST_ANSWER_NO, AnswerStatus.Y))
                .willReturn(Optional.of(mockAnswer));
        mockAnswer.setStatus(AnswerStatus.N);
        given(answerRepository.save(mockAnswer)).willReturn(mockAnswer);

        // when
        Answer deleteAnswer = answerComponentService.deleteAnswer(mockAnswer, mockMember);

        // then
        assertEquals(mockAnswer, deleteAnswer);
    }
}