package bleuauction.bleuauction_be.server.attach.repository;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    List<Attach> findAllByReviewAndFileStatus(Review exitingReview, FileStatus fileStatus);
}
