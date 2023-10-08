package bleuauction.bleuauction_be.server.member.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    // no로 회원찾기
    public Optional<Member> findByMemberNo(Long memberNo) {
        return memberRepository.findById(memberNo);
    }

    // email로 회원찾기
    public Optional<Member> findByMemberEmail(String memberEmail) {
        return memberRepository.findByMemberEmail(memberEmail);
    }

    // email과 pwd로 회원 찾기
    public Optional<Member> findByMemberEmailAndMemberPwd(String memberEmail, String memberPwd) {
        return memberRepository.findByMemberEmailAndMemberPwd(memberEmail, memberPwd);
    }

    // 회원가입
    public Long signUp(Member member) {
        validateDuplicateMember(member);
        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(member.getMemberPwd());
        member.setMemberPwd(encryptedPassword);
        memberRepository.save(member);
        return member.getMemberNo();
    }

    // 이메일 중복검사
    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByMemberEmail(member.getMemberEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    // 회원 수정
    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    // 회원 목록 조희
    public List<Member> list() {
        return memberRepository.findAll();
    }

    // 회원 삭제
    public void delete(Long memberNo) {
        memberRepository.deleteById(memberNo);
    }

    // 암호화 반환
    public String getEncryptPassword(String password) {
        // Implement password encryption logic if needed
        return passwordEncoder.encode(password);
    }
}
