package bleuauction.bleuauction_be.server.member.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService {

  private final MemberRepository memberRepository;

  public void updateMember(Long memberNo, UpdateMemberRequest updateMemberRequest) throws MemberNotFoundException {
    Optional<Member> optionalMember = memberRepository.findById(memberNo);

    if (optionalMember.isPresent()) {
      Member member = optionalMember.get();
      member.setMemberPwd(updateMemberRequest.getMemberPwd());
      member.setMemberName(updateMemberRequest.getMemberName());
      member.setMemberAddr(updateMemberRequest.getMemberAddr());
      member.setMemberZipcode(updateMemberRequest.getMemberZipcode());
      member.setMemberDetailAddr(updateMemberRequest.getMemberDetailAddr());
      member.setMemberPhone(updateMemberRequest.getMemberPhone());
      member.setMemberBank(updateMemberRequest.getMemberBank());
      member.setMemberAccount(updateMemberRequest.getMemberAccount());

      memberRepository.save(member);

    } else {
      throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
    }
  }
}