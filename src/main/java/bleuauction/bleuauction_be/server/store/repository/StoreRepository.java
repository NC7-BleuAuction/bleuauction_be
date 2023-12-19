package bleuauction.bleuauction_be.server.store.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  Page<Store> findAllByStoreStatus(
          @Param("storeStatus") StoreStatus storeStatus,
          Pageable pageable
  );

  Optional<Store> findByMemberNo(Member member);

  Optional<Store> findByStoreName(String storeName);

  boolean existsByStoreName(String storeName);
}