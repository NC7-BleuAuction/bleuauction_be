package bleuauction.bleuauction_be.server.store.repository;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findAllByStatus(StoreStatus status, Pageable pageable);

    Optional<Store> findByMember(Member member);

    Optional<Store> findByStoreName(String storeName);

    boolean existsByStoreName(String storeName);
}
