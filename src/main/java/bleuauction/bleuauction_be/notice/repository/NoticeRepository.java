package bleuauction.bleuauction_be.notice.repository;

import bleuauction.bleuauction_be.notice.entity.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

  @PersistenceContext
  EntityManager em;

  public Long save(Notice notice) {
    em.persist(notice);
    return notice.getNoticeNo();
  }

  public Notice findOne(Long noticeNo) {
    return em.find(Notice.class, noticeNo);
  }

  public List<Notice> findAll(){
    List<Notice> result = em.createQuery("select n from Notice n", Notice.class)
            .getResultList();

    return result;
  }
}
