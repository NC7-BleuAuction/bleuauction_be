package bleuauction.bleuauction_be.server.member.util;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;

public class MemberEntityFactory {
    static {
        Member member =
                Member.builder()
                        .email("test@test.com")
                        .password("testpassword123")
                        .name("테스트이름")
                        .zipCode("14055")
                        .addr("경기도 안양시 동안구 시민대로 327번길 55")
                        .detailAddr("999동1234호")
                        .phone("010-1111-1111")
                        .bankName("시난으냉")
                        .bankAccount("111-111-111111")
                        .category(MemberCategory.S)
                        .status(MemberStatus.Y)
                        .build();
        member.setId(1L);

        mockSellerMember = member;
    }

    public static final Member mockSellerMember;

    public static Member of(String email, String pwd, String name, MemberCategory category) {
        return Member.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .zipCode("14055")
                .addr("경기도 안양시 동안구 시민대로 327번길 55")
                .detailAddr("999동1234호")
                .phone("010-1111-1111")
                .bankName("시난으냉")
                .bankAccount("111-111-111111")
                .category(category)
                .status(MemberStatus.Y)
                .build();
    }
}
