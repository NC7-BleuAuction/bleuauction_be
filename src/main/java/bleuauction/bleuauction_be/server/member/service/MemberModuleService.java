package bleuauction.bleuauction_be.server.member.service;


import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class MemberModuleService {
    private final MemberRepository memberRepository;

    // no로 회원찾기
    public Optional<Member> findByMemberNo(Long memberNo) {
        return memberRepository.findById(memberNo);
    }

    /**
     * MemberNo로 사용자 정보 조회후 반환하며, 존재하지 않는 경우 MemberNotFoundException 발생
     *
     * @param memberNo 사용자 Entity의 Id
     * @return
     */
    public Member findById(Long memberNo) {
        return memberRepository
                .findById(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
    }

    /**
     * Email로 사용자 정보 조회후 반환하며, 존재하지 않는 경우 MemberNotFoundException 발생
     *
     * @param email
     * @return
     */
    public Member findByEmail(String email) {
        return memberRepository
                .findByMemberEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Bad Request Email"));
    }

    /**
     * Page와 Limit를 매개변수로 하여 최근 가입일의 사용자를 전체 조회함
     *
     * @param page 조회 희망 페이지
     * @param limit 조회희망 건수
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Member> findAllMemberByPageableOrderByRegDateDesc(int page, int limit) {
        return memberRepository.findAllByOrderByRegDatetimeDesc(PageRequest.of(page, limit));
    }

    /**
     * 사용자 정보 Insert, Update
     *
     * @param member
     * @return
     */
    public Member save(Member member) {
        log.info(
                "[MemberModuleService] Member Save,Request Member Info : RequestEmail >>> {}, RequestName >>> {} , RequestPhone >>> {}",
                member.getEmail(),
                member.getEmail(),
                member.getPhone());

        return memberRepository.save(member);
    }

    /**
     * 사용자 Email 존재유무 확인
     *
     * @param email 사용자 Email
     * @return
     */
    public boolean isExistsByEmail(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    /**
     * 사용자의 MemberNo가 존재하는지 유무 확인
     *
     * @param memberNo 사용자 Entity의 Id
     * @return
     */
    public boolean isExistsByMemberNo(Long memberNo) {
        return memberRepository.existsById(memberNo);
    }

    /**
     * 사용자 정보 삭제 처리, 해당 사용자가 존재하지 않는 사용자인 경우 MemberNotFoundException 발생
     *
     * @param memberNo
     */
    public void deleteById(Long memberNo) {
        if (this.isExistsByMemberNo(memberNo)) {
            memberRepository.deleteById(memberNo);
            return;
        }
        log.warn("[MemberModuleService] Member Delete Fail, Request MemberNo >>> {}", memberNo);
        throw new MemberNotFoundException(memberNo);
    }
}
