package bleuauction.bleuauction_be.server.answer.service;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.answer.entity.AnswerStatus;
import bleuauction.bleuauction_be.server.answer.repository.AnswerRepository;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.member.service.MemberModuleService;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    private final static int PAGE_ROW_COUNT = 2;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final MemberModuleService memberModuleService;

    @Transactional(readOnly = true)
    public Map<String, Object> selectAnswerList(Long reviewNo, int startPage) {
        Pageable pageable = PageRequest.of(startPage, PAGE_ROW_COUNT, Sort.by(Sort.Order.desc("regDatetime")));

        Page<Answer> page = answerRepository.findByReviewAndStatus(findReviewById(reviewNo), AnswerStatus.Y, pageable);

        List<Answer> answerList = page.getContent();
        long totalRows = page.getTotalElements(); // 전체 행 수
        int totalPages = page.getTotalPages(); // 전체 페이지 수

        Map<String, Object> answerMap = new HashMap<>();
        answerMap.put("answerList", answerList);
        answerMap.put("totalRows", totalRows);
        answerMap.put("totalPages", totalPages);
        log.info("answerMap: ", answerMap);

        return answerMap;
    }

    public Answer addAnswer(TokenMember tokenMember, Answer answer) {
        answer.setMember(memberModuleService.findById(tokenMember.getMemberNo()));
        return answerRepository.save(answer);
    }

    public Answer updateAnswer(TokenMember tokenMember, Answer answer, Member member) throws Exception {
        if (tokenMember.getMemberNo() != member.getId()) {
            throw new Exception("답글 수정 권한이 없습니다!");
        }
        answer.setMember(member);
        Answer exitingAnswer = answerRepository.findByIdAndReviewAndStatus(answer.getId(), answer.getReview(), AnswerStatus.Y)
                .orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));
        exitingAnswer.setContent(answer.getContent());
        return answerRepository.save(exitingAnswer);
    }


    public Answer deleteAnswer(TokenMember tokenMember, Long answerNo, Long memberNo) throws Exception {
        if (tokenMember.getMemberNo() != memberNo) {
            throw new Exception("답글 삭제 권한이 없습니다!");
        }

        Answer exitingAnswer = answerRepository.findByIdAndStatus(answerNo, AnswerStatus.Y).orElseThrow(() -> new Exception("해당 답글이 존재하지 않습니다!"));
        exitingAnswer.setStatus(AnswerStatus.N);
        return answerRepository.save(exitingAnswer);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다!"));
    }

}
