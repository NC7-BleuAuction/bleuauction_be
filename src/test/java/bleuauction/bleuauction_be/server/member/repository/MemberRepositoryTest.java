package bleuauction.bleuauction_be.server.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    @DisplayName("사용자 저장테스트")
    public void saveTest() {
        // given
        Member beforeSaveMember =
                Member.builder()
                        .memberEmail("test@test.com")
                        .memberPwd(passwordEncoder.encode("testPassword"))
                        .memberName("테스트계정")
                        .memberZipcode("11111")
                        .memberAddr("서울시 강남구 서초대로")
                        .memberDetailAddr("비트캠프 5층")
                        .memberPhone("010-1111-1111")
                        .memberBank("신한은행")
                        .memberAccount("010-111-123456")
                        .memberCategory(MemberCategory.M)
                        .memberStatus(MemberStatus.Y)
                        .build();

        int beforeSaveMemberCount = memberRepository.findAll().size();
        // when
        Member afterSaveMember = memberRepository.save(beforeSaveMember);

        // then
        assertNotNull(afterSaveMember.getId());
        assertEquals(memberRepository.findAll().size(), beforeSaveMemberCount + 1);
        assertEquals(memberRepository.findById(afterSaveMember.getId()).get(), afterSaveMember);
    }

    @Test
    @DisplayName("메일계정을 파라미터로 제공할때, 존재하여서 true 제공받는지 확인")
    public void existsByMemberEmailTestResultTrue() {
        // given
        String testMail = "test@test.com";
        memberRepository.save(
                Member.builder()
                        .memberEmail(testMail)
                        .memberPwd(passwordEncoder.encode("testPassword"))
                        .memberName("테스트계정")
                        .memberZipcode("11111")
                        .memberAddr("서울시 강남구 서초대로")
                        .memberDetailAddr("비트캠프 5층")
                        .memberPhone("010-1111-1111")
                        .memberBank("신한은행")
                        .memberAccount("010-111-123456")
                        .memberCategory(MemberCategory.M)
                        .memberStatus(MemberStatus.Y)
                        .build());

        // when && then
        assertTrue(memberRepository.existsByEmail(testMail));
    }

    @Test
    @DisplayName("메일계정을 파라미터로 제공할때, 존재하지 않아 false 제공받는지 확인")
    public void existsByMemberEmailTestResultFalse() {
        // given
        String testMail = "test@test.com";

        // when && then
        assertFalse(memberRepository.existsByEmail(testMail));
    }

    @Test
    @DisplayName("메일계정을 파라미터로 제공할때, 해당메일이 존재하여 메일객체를 반환받는지 확인")
    public void findByMemberEmailResultMember() {
        // given
        String testMail = "test@test.com";
        memberRepository.save(
                Member.builder()
                        .memberEmail(testMail)
                        .memberPwd(passwordEncoder.encode("testPassword"))
                        .memberName("테스트계정")
                        .memberZipcode("11111")
                        .memberAddr("서울시 강남구 서초대로")
                        .memberDetailAddr("비트캠프 5층")
                        .memberPhone("010-1111-1111")
                        .memberBank("신한은행")
                        .memberAccount("010-111-123456")
                        .memberCategory(MemberCategory.M)
                        .memberStatus(MemberStatus.Y)
                        .build());

        // when
        Optional<Member> result = memberRepository.findByEmail(testMail);

        // then
        assertTrue(result.isPresent());
        assertEquals(result.get().getClass(), Member.class);
        assertEquals(result.get().getEmail(), testMail);
    }

    @Test
    @DisplayName("메일계정을 파라미터로 제공할때, 해당메일이 존재하여 메일객체를 반환받는지 확인")
    public void findByMemberEmailResultNull() {
        // given
        String testMail = "test@test.com";

        // when
        Optional<Member> result = memberRepository.findByEmail(testMail);

        // then
        assertFalse(result.isPresent());
        assertThrows(NoSuchElementException.class, () -> result.get());
    }
}
