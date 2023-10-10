package bleuauction.bleuauction_be.server.member.repository;

import bleuauction.bleuauction_be.server.member.entity.Member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByMemberEmailAndMemberPwd(String memberEmail, String memberPwd);

  Optional<Member> findBymemberEmail(String memberEmail);
  List<Member> findAll();

}
