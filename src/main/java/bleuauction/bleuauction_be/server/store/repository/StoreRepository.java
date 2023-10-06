package bleuauction.bleuauction_be.server.store.repository;

import bleuauction.bleuauction_be.server.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findAll();
}
