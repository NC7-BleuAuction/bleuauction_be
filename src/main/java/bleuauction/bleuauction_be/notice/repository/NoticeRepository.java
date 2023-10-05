package bleuauction.bleuauction_be.notice.repository;

import bleuauction.bleuauction_be.notice.entity.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class NoticeRepository {

  private final EntityManager em;

  public void save(Notice notice) {
    em.persist(notice);
  }

  public Notice findOne(Long noticeNo) {
    return em.find(Notice.class, noticeNo);
  }

  public List<Notice> findAll() {
    List<Notice> result = em.createQuery("select n from Notice n", Notice.class)
            .getResultList();
    return result;
  }


}
