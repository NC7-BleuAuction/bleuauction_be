package bleuauction.bleuauction_be.server.member.repository;


import bleuauction.bleuauction_be.server.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Page<Member> findAllByOrderByRegDatetimeDesc(Pageable pageable);

    boolean existsByEmail(String email);
}
