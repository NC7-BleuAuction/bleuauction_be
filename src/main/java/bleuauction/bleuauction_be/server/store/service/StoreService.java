package bleuauction.bleuauction_be.server.store.service;

import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public List<Store> selectStoreList() {
    return storeRepository.findAll();
  }

  public Optional<Store> findByStoreNo(Long storeNo) {
    return storeRepository.findBystoreNo(storeNo);
  }
}