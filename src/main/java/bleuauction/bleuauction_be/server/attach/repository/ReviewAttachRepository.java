package bleuauction.bleuauction_be.server.attach.repository;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.entity.ReviewAttach;
import bleuauction.bleuauction_be.server.review.entity.Review;
import java.util.List;

public interface ReviewAttachRepository extends AttachRepository<ReviewAttach> {
    List<Attach> findAllByReviewAndFileStatus(Review exitingReview, FileStatus fileStatus);
}
