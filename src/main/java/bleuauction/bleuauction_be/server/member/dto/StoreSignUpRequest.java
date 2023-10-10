package bleuauction.bleuauction_be.server.member.dto;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreSignUpRequest {

    @Email
    @NotEmpty(message = "Email이 입력되지 않았습니다.")
    private String memberEmail;

    @NotEmpty(message = "패스워드가 입력되지 않았습니다.")
    private String memberPwd;

    @NotEmpty(message = "사용자 이름이 입력되지 않았습니다.")
    private String memberName;

    @NotEmpty(message = "대표번호가 입력되지 않았습니다.")
    private String memberPhone;

    @NotEmpty(message = "은행명이 입력되지 않았습니다.")
    private String memberBank; //은행명

    @NotEmpty(message = "계좌번호가 입력되지 않았습니다.")
    private String memberAccount; //계좌번호

    private MemberCategory memberCategory = MemberCategory.S;

    private MemberStatus memberStatus = MemberStatus.Y;

    @NotEmpty(message = "시장명이 입력되지 않았습니다.")
    private String marketName;

    @NotEmpty(message = "가게명이 입력되지 않았습니다.")
    private String storeName;

    @NotEmpty(message = "사업자등록번호가 입력되지 않았습니다.")
    private String licenseNo;

    @NotEmpty(message = "기본주소가 입력되지 않았습니다.")
    private String storeAddr;

    @NotEmpty(message = "상세주소가 입력되지 않았습니다.")
    private String storeDetailAddr;

    @NotEmpty(message = "우편번호가 입력되지 않았습니다.")
    private String storeZipCode;

    /**
     * Request로 받은 정보를 바탕으로 Member Entity 반환
     * @return
     */
    public Member getMemberEntity() {
        Member member = new Member();
        member.setMemberEmail(this.memberEmail);
        member.setMemberPwd(this.memberPwd);
        member.setMemberName(this.memberName);
        member.setMemberZipcode(this.storeZipCode);
        member.setMemberAddr(this.storeAddr);
        member.setMemberDetailAddr(this.storeDetailAddr);
        member.setMemberPhone(this.memberPhone);
        member.setMemberBank(this.memberBank);
        member.setMemberAccount(this.memberAccount);
        member.setMemberCategory(this.memberCategory);
        member.setMemberStatus(this.memberStatus);

        return member;
    }

    public Store getStoreEntity(Member signUpMemberEntity) {
        return Store.builder()
                .memberNo(signUpMemberEntity.getMemberNo())
                .marketName(this.marketName)
                .storeName(this.storeName)
                .licenseNo(this.licenseNo)
                .storeZipcode(this.storeZipCode)
                .storeAddr(this.storeAddr)
                .storeDetailAddr(this.storeDetailAddr)
                .storeStatus(StoreStatus.Y)
                .build();
    }
}
