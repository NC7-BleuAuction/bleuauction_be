package bleuauction.bleuauction_be.notice.repository;

import bleuauction.bleuauction_be.notice.domain.Notice;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

  private final EntityManager em;

  public void save(Notice notice) {em.persist(notice);}// 노티스 저장

  public Notice findOne(int notice_no) {
    return em.find(Notice.class, notice_no);
  }

  public List<Notice> findAll(){
    List<Notice> result = em.createQuery("select n from ba_notice n", Notice.class)
            .getResultList();

    return result;
  }
}
