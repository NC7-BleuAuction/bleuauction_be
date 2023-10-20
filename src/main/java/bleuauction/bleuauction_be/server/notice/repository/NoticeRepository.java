package bleuauction.bleuauction_be.server.notice.repository;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class NoticeRepository {

  private final EntityManager em;

  public Notice save(Notice notice) {
    em.persist(notice);
    return notice;
  }

  public Notice findOne(Long noticeNo) {
    return em.find(Notice.class, noticeNo);
  }

  public List<Notice> findAll() {
    List<Notice> result = em.createQuery("select n from Notice n", Notice.class)
            .getResultList();
    return result;
  }


  public List<Notice> findByNoticeStatus(NoticeStatus status) {
    List<Notice> result = em.createQuery("select n from Notice n where n.noticeStatus = :status", Notice.class)
            .setParameter("status", status)
            .getResultList();
    return result;
  }
}
