package bleuauction.bleuauction_be.server.attach.repository;

import bleuauction.bleuauction_be.server.attach.entity.Attach;

import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

  Attach findByFileNo(Long fileNo);

  Optional<List<Attach>> findAllByReviewAndFileStatus(Review exitingReview, FileStatus fileStatus);
}
