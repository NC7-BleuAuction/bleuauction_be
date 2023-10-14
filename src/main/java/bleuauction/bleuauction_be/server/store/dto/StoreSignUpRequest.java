package bleuauction.bleuauction_be.server.store.dto;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;

import jakarta.validation.constraints.NotEmpty;
import java.sql.Time;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreSignUpRequest {

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
    private String storeZipcode;

    private Time weekdayStartTime;

    private Time weekdayEndTime;

    private Time weekendStartTime;

    private Time weekendEndTime;

    public Store getStoreEntity(Member signUpMemberEntity) {
        return Store.builder()
                .memberNo(signUpMemberEntity.getMemberNo())
                .marketName(this.marketName)
                .storeName(this.storeName)
                .licenseNo(this.licenseNo)
                .storeZipcode(this.storeZipcode)
                .storeAddr(this.storeAddr)
                .storeDetailAddr(this.storeDetailAddr)
                .storeStatus(StoreStatus.Y)
                .weekdayStartTime(this.weekdayStartTime)
                .weekdayEndTime(this.weekdayEndTime)
                .weekendStartTime(this.weekendStartTime)
                .weekendEndTime(this.weekendEndTime)
                .build();
    }
}
