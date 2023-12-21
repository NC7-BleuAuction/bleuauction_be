package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.review.entity.Review;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("REVIEW")
public class ReviewAttach extends Attach {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_no")
    private Review review;

    public ReviewAttach(AttachVO attachVO) {
        super(attachVO);
    }

    public ReviewAttach(AttachVO attachVO, Review review) {
        this(attachVO);
        this.review = review;
    }

    public void setReview(Review review) {
        this.review = review;
        review.getAttaches().add(this);
    }
}
