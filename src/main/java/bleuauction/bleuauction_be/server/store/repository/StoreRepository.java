package bleuauction.bleuauction_be.server.store.repository;

import bleuauction.bleuauction_be.server.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findAll();

  Optional<Store> findBystoreNo(Long storeNo);
  Optional<Store> findBystoreName(String storeName);
}