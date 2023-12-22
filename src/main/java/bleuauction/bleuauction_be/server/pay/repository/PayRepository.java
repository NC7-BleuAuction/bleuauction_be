package bleuauction.bleuauction_be.server.pay.repository;


import bleuauction.bleuauction_be.server.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {}
