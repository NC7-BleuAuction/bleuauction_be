package bleuauction.bleuauction_be.server.pay.repository;


import bleuauction.bleuauction_be.server.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {}
