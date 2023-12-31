package bleuauction.bleuauction_be.server.member.service;


import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.dto.LoginResponseDto;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Address;
import bleuauction.bleuauction_be.server.member.entity.BankAccount;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.member.exception.DuplicateMemberEmailException;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class MemberComponentService {
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AttachComponentService attachComponentService;
    private final MemberModuleService memberModuleService;

    // no로 회원찾기
    // TODO : 차후 삭제 필요
    public Optional<Member> findByMemberNo(Long memberNo) {
        return memberModuleService.findByMemberNo(memberNo);
    }

    /**
     * 로그인 처리 로직
     *
     * @param email 로그인을 요청한 사용자 ID
     * @param requestPassword 로그인을 요청한 사용자의 패스워드
     * @return
     */
    public LoginResponseDto login(String email, String requestPassword) {
        Member member = memberModuleService.findByEmail(email);

        if (!passwordEncoder.matches(requestPassword, member.getPassword())) {
            throw new MemberNotFoundException("Bad Request Password");
        }

        // 로그인 정상처리에 대한 반환 정보 추출
        TokenMember tokenMember =
                TokenMember.builder()
                        .memberNo(member.getId())
                        .memberEmail(member.getEmail())
                        .memberName(member.getName())
                        .memberCategory(member.getCategory())
                        .build();
        String accessToken = jwtUtils.createAccessToken(tokenMember);
        String refreshToken = jwtUtils.createRefreshToken(tokenMember, accessToken);

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
        Page<Member> result =
                memberModuleService.findAllMemberByPageableOrderByRegDateDesc(page, limit);
        return Map.of(
                "totalPage", result.getTotalPages(),
                "totalElements", result.getTotalElements(),
                "data", result.getContent());
    }

    /**
     * 사용자 회원가입
     *
     * @param member 회원가입을 위한 사용자 Entity
     * @return
     */
    public Member signUp(Member member) {
        validateDuplicateMember(member.getEmail());

        log.info(
                "[MembeComponentrService] Member SignUp : RequestEmail >>> {}, RequestName >>> {} , RequestPhone >>> {}",
                member.getEmail(),
                member.getEmail(),
                member.getPhone());

        // Password Encoded(암호화)
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberModuleService.save(member);
    }

    /**
     * 회원탈퇴 로직
     *
     * @param tokenMember 로그인한 사용자의 토큰정보 내부의 값
     */
    @Transactional
    public void withDrawMember(TokenMember tokenMember) {
        Member member = memberModuleService.findById(tokenMember.getMemberNo());
        member.setStatus(MemberStatus.N);
        log.info("회원이 성공적으로 탈퇴되었습니다. 회원번호: {}", member.getId());

        // 여기에서 토큰을 무효화하는 로직을 추가해야 합니다.
        // 토큰을 무효화하고 클라이언트 측에서도 토큰을 삭제하는 방법을 사용하십시오.
    }

    @Transactional
    public Member updateMember(TokenMember tokenMember, UpdateMemberRequest request) {
        Member loginUser = memberModuleService.findById(tokenMember.getMemberNo());

        // Profile Image ObjectStorage에 저장
        if (!request.getProfileImage().isEmpty()) {
            attachComponentService.saveWithMember(
                    loginUser, FileUploadUsage.MEMBER, request.getProfileImage());
        }

        loginUser.setPassword(passwordEncoder.encode(request.getMemberPwd()));
        loginUser.setName(request.getMemberName());
        loginUser.setAddress(
                Address.builder()
                        .zipCode(request.getMemberZipcode())
                        .addr(request.getMemberAddr())
                        .detailAddr(request.getMemberDetailAddr())
                        .build());
        loginUser.setBankAccount(
                BankAccount.builder()
                        .bankName(request.getMemberBank())
                        .bankAccount(request.getMemberAccount())
                        .build());
        loginUser.setPhone(request.getMemberPhone());

        return memberModuleService.save(loginUser);
    }

    /**
     * Email 중복검사 실시 로직으로, Email이 존재하는 경우 DuplicatememberEmailException 발생
     *
     * @param email 가입이 되어있는지 확인을 진행할 Email 문자열
     */
    public void validateDuplicateMember(String email) {
        if (memberModuleService.isExistsByEmail(email)) {
            throw new DuplicateMemberEmailException(email);
        }
    }

    /**
     * 회원의 프로필 이미지를 삭제하는 기능으로 <br>
     * 해당 기능은 Controller가 적합함. <br>
     * [TODO] : 현재 해당 기능의 문제점은 인증 인가없이 그냥 fileNo를 입력할때 삭제가 된다는 점, 그러므로 타인이 삭제시킬수도 있음. 추후 보완이 필요하다.
     *
     * @param fileNo
     * @return
     */
    public ResponseEntity<String> deleteProfileImage(Long fileNo) {
        if (FileStatus.N.equals(
                attachComponentService.changeFileStatusDeleteByFileNo(fileNo).getFileStatus())) {
            return ResponseEntity.ok("Profile Image Delete Success");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Profile Image Delete Failed");
    }
}
