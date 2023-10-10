package bleuauction.bleuauction_be.server.member.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByMemberEmailAndMemberPwd(String memberEmail, String memberPwd);

  List<Member> findAll();

    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberPwd(String memberPwd);
}
