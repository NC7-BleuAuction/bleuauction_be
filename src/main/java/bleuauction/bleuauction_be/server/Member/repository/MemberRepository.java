package bleuauction.bleuauction_be.server.Member.repository;

import bleuauction.bleuauction_be.server.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
