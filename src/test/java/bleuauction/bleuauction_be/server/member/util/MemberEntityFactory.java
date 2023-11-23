package bleuauction.bleuauction_be.server.member.util;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;

public class MemberEntityFactory {

    public static Member of(String email, String pwd, String name, MemberCategory category){
        return Member.builder()
                .memberEmail(email)
                .memberPwd(pwd)
                .memberName(name)
                .memberZipcode("14055")
                .memberAddr("경기도 안양시 동안구 시민대로 327번길 55")
                .memberDetailAddr("999동1234호")
                .memberPhone("010-1111-1111")
                .memberBank("시난으냉")
                .memberAccount("111-111-111111")
                .memberCategory(category)
                .memberStatus(MemberStatus.Y)
                .build();
    }

}
