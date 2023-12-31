package bleuauction.bleuauction_be.server.answer.entity;

import static bleuauction.bleuauction_be.server.answer.entity.AnswerStatus.N;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.common.entity.AbstractTimeStamp;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ba_answer")
@NoArgsConstructor(access = PROTECTED)
public class Answer extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answer_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_no")
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Lob private String content;

    @Enumerated(STRING)
    private AnswerStatus status;

    @Builder
    public Answer(Review review, Member member, String content, AnswerStatus status) {
        this.review = review;
        this.member = member;
        this.content = content;
        this.status = status;
    }

    // === 연관관계 편의 메서드 ===
    public void setReview(Review review) {
        this.review = review;
        review.getAnswers().add(this);
    }

    // === 비즈니스 로직 ===
    public void deleteAnswer() {
        this.status = N;
    }
}
