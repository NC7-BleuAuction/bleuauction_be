package bleuauction.bleuauction_be.server.attach.repository;

import bleuauction.bleuauction_be.server.attach.entity.Attach;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachRepository extends JpaRepository<Attach, Long> {

  Attach findByFileNo(Long fileNo);

}
