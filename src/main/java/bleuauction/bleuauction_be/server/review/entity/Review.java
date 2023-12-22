package bleuauction.bleuauction_be.server.review.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import bleuauction.bleuauction_be.server.answer.entity.Answer;
import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.ReviewAttach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_review")
public class Review {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_no")
    private Long id;

    @Lob private String reviewContent;

    @Enumerated(STRING)
    private ReviewFreshness reviewFreshness;

    @Enumerated(STRING)
    private ReviewStatus reviewStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_no")
    private Store store;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToMany(mappedBy = "review", fetch = LAZY, cascade = ALL)
    private List<ReviewAttach> attaches = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = LAZY, cascade = ALL)
    private List<Answer> answers = new ArrayList<>();

    @CreationTimestamp private Timestamp regDatetime;

    @UpdateTimestamp private Timestamp mdfDatetime;

    @Builder
    public Review(
            Store store,
            Member member,
            String reviewContent,
            ReviewFreshness reviewFreshness,
            List<Attach> attaches,
            ReviewStatus reviewStatus) {
        this.store = store;
        this.member = member;
        this.reviewContent = reviewContent;
        this.reviewFreshness = reviewFreshness;
        this.reviewStatus = reviewStatus;
    }

    public void addStore(Store store) {
        this.store = store;
        store.getReviews().add(this);
    }

    public void addMember(Member member) {
        this.member = member;
        member.getReviews().add(this);
    }
}
