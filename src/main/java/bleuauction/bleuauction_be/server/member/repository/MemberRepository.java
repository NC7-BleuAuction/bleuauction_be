package bleuauction.bleuauction_be.server.member.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberEmail(String memberEmail);

    Page<Member> findAllByOrderByRegDatetimeDesc(Pageable pageable);

    boolean existsByMemberEmail(String email);
}
