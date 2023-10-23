package bleuauction.bleuauction_be.server.store.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  @Query("SELECT s " +
          "FROM Store s " +
          "WHERE s.storeStatus = :storeStatus ")
  List<Store> findAllByStoreStatus(
          @Param("storeStatus") StoreStatus storeStatus,
          Pageable pageable
  );

  Optional<Store> findByMemberNo(Member member);

  Optional<Store> findBystoreNo(Long storeNo);
  Optional<Store> findBystoreName(String storeName);
}