package bleuauction.bleuauction_be.server.member.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.dto.LoginResponseDto;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.member.exception.DuplicateMemberEmailException;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final CreateJwt createJwt;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // no로 회원찾기
    public Optional<Member> findByMemberNo(Long memberNo) {
        return memberRepository.findById(memberNo);
    }

    /**
     * Id기준 사용자 정보 조회 및 존재하지 않는 경우, Exception
     *
     * @param memberNo 사용자 Entity의 Id
     * @return
     */
    public Member findMemberById(Long memberNo) {
        return memberFindById(memberNo);
    }

    /**
     * 로그인 처리 로직
     * @param email 로그인을 요청한 사용자 ID
     * @param requestPassword 로그인을 요청한 사용자의 패스워드
     * @return
     */
    public LoginResponseDto login(String email, String requestPassword) {
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Bad Request Email"));

        if(!passwordEncoder.matches(requestPassword, member.getMemberPwd())){
            throw new MemberNotFoundException("Bad Request Password");
        }

        //로그인 정상처리에 대한 반환 정보 추출
        TokenMember tokenMember = TokenMember.builder()
                .memberNo(member.getMemberNo())
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .memberCategory(member.getMemberCategory().name())
                .build();
        String accessToken = createJwt.createAccessToken(tokenMember);
        String refreshToken = createJwt.createRefreshToken(tokenMember, accessToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .loginUser(member)
                .build();
    }

    /**
     * Page와 Limit를 매개변수로 하여 최근 가입일의 사용자를 전체 조회함
     *
     * @param page 조회 희망 페이지
     * @param limit 조회희망 건수
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findAllMemberByPageableOrderByRegDateDesc(int page, int limit) {
        Page<Member> result = memberRepository.findAllByOrderByRegDatetimeDesc(PageRequest.of(page, limit));
        return Map.of(
                "totalPage", result.getTotalPages(),
                "totalElements", result.getTotalElements(),
                "data", result.getContent()
        );
    }

    /**
     * 사용자 회원가입
     * @param member 회원가입을 위한 사용자 Entity
     * @return
     */
    public Member signUp(Member member) {
        validateDuplicateMember(member.getMemberEmail());

        log.info("[MemberService] Member SignUp : RequestEmail >>> {}, RequestName >>> {} , RequestPhone >>> {}",
                member.getMemberEmail(), member.getMemberEmail(), member.getMemberPhone());

        // Password Encoded(암호화)
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
        return memberRepository.save(member);
    }

    /**
     * 회원탈퇴 로직
     * @param tokenMember 로그인한 사용자의 토큰정보 내부의 값
     */
    @Transactional
    public void withDrawMember(TokenMember tokenMember) {
        Member member = memberFindById(tokenMember.getMemberNo());
        member.setMemberStatus(MemberStatus.N);
        log.info("회원이 성공적으로 탈퇴되었습니다. 회원번호: {}", member.getMemberNo());

        // 여기에서 토큰을 무효화하는 로직을 추가해야 합니다.
        // 토큰을 무효화하고 클라이언트 측에서도 토큰을 삭제하는 방법을 사용하십시오.
    }

    /**
     * 사용자 정보 삭제 처리, 해당 사용자가 존재하지 않는 사용자인 경우 MemberNotFoundException 발생
     * @param memberNo 사용자 Entity의 Id
     */
    public void deleteMemberById(Long memberNo) {
        if(memberRepository.existsById(memberNo)) {
            memberRepository.deleteById(memberNo);
            return;
        }
        throw new MemberNotFoundException(memberNo);
    }

    /**
     * 사용자 Email 존재유무 확인
     * @param email 사용자 Email
     * @return
     */
    public boolean duplicateMemberEmail(String email){
        return memberRepository.existsByMemberEmail(email);
    }

    /**
     * 사용자의 MemberNo가 존재하는지 유뭏 확인
     * @param memberNo 사용자 Entity의 Id
     * @return
     */
    public boolean isExistsByMemberNo(Long memberNo) {
        return memberRepository.existsById(memberNo);
    }

    @Transactional
    public Member updateMember(TokenMember tokenMember, UpdateMemberRequest request, Attach profileImage) {
        Member loginUser = memberFindById(tokenMember.getMemberNo());
        if(profileImage != null) {
            profileImage.setMemberNo(loginUser);
        }

        loginUser.setMemberPwd(passwordEncoder.encode(request.getMemberPwd()));
        loginUser.setMemberName(request.getMemberName());
        loginUser.setMemberAddr(request.getMemberAddr());
        loginUser.setMemberZipcode(request.getMemberZipcode());
        loginUser.setMemberDetailAddr(request.getMemberDetailAddr());
        loginUser.setMemberPhone(request.getMemberPhone());
        loginUser.setMemberBank(request.getMemberBank());
        loginUser.setMemberAccount(request.getMemberAccount());
        loginUser.addAttaches(profileImage);


        memberRepository.save(loginUser);
        return loginUser;
    }


    /**
     * Email 중복검사 실시 로직으로, Email이 존재하는 경우 DuplicatememberEmailException 발생
     *
     * @param email 가입이 되어있는지 확인을 진행할 Email 문자열
     */
    private void validateDuplicateMember(String email) {
        if (this.duplicateMemberEmail(email)) {
            throw new DuplicateMemberEmailException(email);
        }
    }

    /**
     * MemberNo로 사용자 정보 조회후 반환하며, 존재하지 않는 경우 MemberNotFoundException 발생
     *
     * @param memberNo 사용자 Entity의 Id
     * @return
     */
    private Member memberFindById(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
    }
}
