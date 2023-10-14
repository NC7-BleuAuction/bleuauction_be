package bleuauction.bleuauction_be.server.member.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;

    // no로 회원찾기
    public Optional<Member> findByMemberNo(Long memberNo) {
        return memberRepository.findById(memberNo);
    }

    // email로 회원찾기
    public Optional<Member> findByMemberEmail(String memberEmail) {
        return memberRepository.findByMemberEmail(memberEmail);
    }
    // pwd로 회원찾기
    public Optional<Member> findByMemberPwd(String memberPwd) {
        return memberRepository.findByMemberPwd(memberPwd);
    }
    // email과 pwd로 회원 찾기
    public Optional<Member> findByMemberEmailAndMemberPwd(String memberEmail, String memberPwd) {
        return memberRepository.findByMemberEmailAndMemberPwd(memberEmail, memberPwd);
    }

    // 회원가입
    public Member signUp(Member member) {
        validateDuplicateMember(member);
        // Encrypt the password
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
        return memberRepository.save(member);
    }

    // 이메일 중복검사
    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByMemberEmail(member.getMemberEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
    // 회원 목록 조희
    public List<Member> list() {
        return memberRepository.findAll();
    }

    // 회원 삭제
    public void delete(Long memberNo) {
        memberRepository.deleteById(memberNo);
    }

    // 회원 저장
    public Member saveMember(Member member) throws Exception {
        return memberRepository.save(member);
    }

    // 암호화 반환
    public String getEncryptPassword(String password) {
        // Implement password encryption logic if needed
        return passwordEncoder.encode(password);
    }
}
