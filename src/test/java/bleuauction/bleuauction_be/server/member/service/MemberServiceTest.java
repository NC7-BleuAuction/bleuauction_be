package bleuauction.bleuauction_be.server.member.service;


import bleuauction.bleuauction_be.server.member.dto.LoginResponseDto;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.exception.DuplicateMemberEmailException;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import bleuauction.bleuauction_be.server.util.TokenMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private CreateJwt createJwt;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    private final String TEST_MAIL = "test@test.com";
    private final String TEST_PWD = "testpassword123!";
    private final String TEST_NAME = "테스트 이름";

    @Test
    @DisplayName("사용자의 NO를 파라미터로 제공할 때, 사용자가 존재하는 경우 Optional<Member>에 사용자가 존재하는 객체를 반환한다")
    void whenGivenMemberNo_thenReturnOptionalInMemberObject() {
        // given
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        member.setMemberNo(1L);

        given(memberRepository.findById(member.getMemberNo())).willReturn(Optional.of(member));


        // when
        Optional<Member> optionalFindMember = memberService.findByMemberNo(member.getMemberNo());

        // then
        assertTrue(optionalFindMember.isPresent());

        Member findMember = optionalFindMember.get();
        assertEquals(TEST_MAIL, findMember.getMemberEmail());
        assertEquals(TEST_PWD, findMember.getMemberPwd());
        assertEquals(TEST_NAME, findMember.getMemberName());
    }

    @Test
    @DisplayName("사용자의 NO를 파라미터로 제공할 때, 사용자가 존재하지 않는 경우 사용자가 존재하지 않는 Optional 객체를 반환한다")
    void whenGivenMemberNo_thenReturnOptionalInEmptyObject() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Optional<Member> optionalFindMember = memberService.findByMemberNo(1L);

        // then
        assertTrue(optionalFindMember.isEmpty());
    }

    @Test
    @DisplayName("사용자의 NO를 파라미터로 제공할 때, 사용자가 존재하는 경우 Optional<Member>에 사용자가 존재하는 객체를 반환한다")
    void whenGivenMemberNo_thenReturnMemberObject() {
        // given
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        member.setMemberNo(1L);

        given(memberRepository.findById(member.getMemberNo())).willReturn(Optional.of(member));


        // when
        Member findMember = memberService.findMemberById(member.getMemberNo());

        // then
        assertEquals(TEST_MAIL, findMember.getMemberEmail());
        assertEquals(TEST_PWD, findMember.getMemberPwd());
        assertEquals(TEST_NAME, findMember.getMemberName());
    }

    @Test
    @DisplayName("사용자의 NO를 파라미터로 제공할 때, 사용자가 존재하지 않는 경우 MemberNotFoundException이 발생한다")
    void whenGivenMemberNo_thenThrowMemberNotFoundException() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        // when && then
        assertThrows(MemberNotFoundException.class, () -> memberService.findMemberById(1L));
    }

    @Test
    @DisplayName("로그인을 요청 할 때 파라미터로 전달한 Email이 없는 경우 MemberNotFoundException이 발생한다")
    void whenGivenLoginEmailIsEmpty_ThenThrowMemberNotFoundException() {
        //given
        given(memberRepository.findByMemberEmail(TEST_MAIL)).willReturn(Optional.empty());

        // when && then
        MemberNotFoundException e = assertThrows(MemberNotFoundException.class, () -> memberService.login(TEST_MAIL, TEST_PWD));
        assertEquals( e.getMessage(), "[MemberNotFoundException] Not Found Member Bad Request Email");
    }

    @Test
    @DisplayName("로그인을 요청 할 때 파라미터로 전달한 Password가 일치하지 않는 경우 MemberNotFoundException이 발생한다")
    void whenGivenPasswordNotMatch_ThenThrowMemberNotFoundException() {
        //given
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        member.setMemberNo(1L);

        given(memberRepository.findByMemberEmail(TEST_MAIL)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(TEST_PWD, member.getMemberPwd())).willReturn(false);

        // when && then
        MemberNotFoundException e = assertThrows(MemberNotFoundException.class, () -> memberService.login(TEST_MAIL, TEST_PWD));
        assertEquals( e.getMessage(), "[MemberNotFoundException] Not Found Member Bad Request Password");
    }

    @Test
    @DisplayName("로그인을 요청 할 때 메일과 패스워드가 일치하는 경우, LoginResponseDto에 Token정보와 사용자 정보를 담아 Return한다")
    void whenGivenEmailAndPasswordMatch_ThenReturnLoginResponseDto() {
        //given
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        member.setMemberNo(1L);

        String accessToken = "testAccessTokenTest@test.com테스트토큰";
        String refreshToken =  "testRefreshTokenTest@test.com테스트토큰";

        given(memberRepository.findByMemberEmail(TEST_MAIL)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(TEST_PWD, member.getMemberPwd())).willReturn(true);
        given(createJwt.createAccessToken(any(TokenMember.class))).willReturn(accessToken);
        given(createJwt.createRefreshToken(any(TokenMember.class), any(String.class))).willReturn(refreshToken);

        // when
        LoginResponseDto result = memberService.login(TEST_MAIL, TEST_PWD);

        // then
        assertNotNull(result);
        assertEquals(result.getAccessToken(), accessToken);
        assertEquals(result.getRefreshToken(), refreshToken);
        assertEquals(result.getLoginUser(), member);
    }

    @Test
    @DisplayName("Page와 Limit를 제공할 때 가입한 사용자가 없는 경우 없는 정보를 반환한다")
    void whenGivenPageAndLimit_thenReturnNoSizeList() {
        // given
        given(memberRepository.findAllByOrderByRegDatetimeDesc(any(PageRequest.class))).willReturn(Page.empty());

        // when
        Map<String, Object> result = memberService.findAllMemberByPageableOrderByRegDateDesc(0, 10);

        System.out.println(result.get("totalPage"));
        System.out.println(result.get("totalElements"));

        // then
        assertEquals(result.get("totalPage"), 1);
        assertEquals(result.get("totalElements"), 0L);
    }

    @Test
    @DisplayName("회원가입을 진행 할 때 이미 사용된 메일인 경우에는 DuplicateMemberEmailException이 발생한다")
    void whenSignGivenEmailExistsInDB_thenThrowDuplicateMemberEmailException() {
        // given
        given(memberRepository.existsByMemberEmail(TEST_MAIL)).willReturn(true);
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);

        // when & then
        DuplicateMemberEmailException e = assertThrows(DuplicateMemberEmailException.class, () -> memberService.signUp(member));
        assertEquals(e.getMessage(), "[DuplicateMemberEmailException] Duplicate Member Email, requestEmail >>> " + TEST_MAIL);
    }

    @Test
    @DisplayName("회원가입을 진행 할 때 사용되지 않은 메일인 경우에는 회원가입이 진행후 Member객체가 반환된다")
    void whenSignGivenEmailNotExistsInDB_thenReturnMember() {
        // given
        Member member = MemberEntityFactory.of(TEST_MAIL, TEST_PWD, TEST_NAME, MemberCategory.M);
        String encodePassword = UUID.randomUUID().toString();

        given(memberRepository.existsByMemberEmail(TEST_MAIL)).willReturn(false);
        given(passwordEncoder.encode(member.getMemberPwd())).willReturn(encodePassword);

        given(memberRepository.save(member)).willAnswer(invocation -> {
            Member param = invocation.getArgument(0);
            param.setMemberNo(1L);
            return param;
        });

        // when
        Member signUpMember = memberService.signUp(member);

        // then
        assertEquals(signUpMember.getMemberNo(), 1L);
        assertEquals(signUpMember.getMemberPwd(), encodePassword);

    }

}

