package bleuauction.bleuauction_be.server.store.dto;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;

import bleuauction.bleuauction_be.server.common.utils.CustomTimeDeserializer;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private TokenMember tokenMember;

    private Long memberNo;

    private String memberEmail;

    @NotNull
    private String memberName;

    @NotNull
    private String memberZipcode;

    @NotNull
    private String memberAddr;

    @NotNull
    private String memberDetailAddr;

    @NotNull
    private String memberPhone;

    private Long storeNo;

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

    @JsonDeserialize(using = CustomTimeDeserializer.class)
    private Time weekdayStartTime;

    @JsonDeserialize(using = CustomTimeDeserializer.class)
    private Time weekdayEndTime;

    @JsonDeserialize(using = CustomTimeDeserializer.class)
    private Time weekendStartTime;

    @JsonDeserialize(using = CustomTimeDeserializer.class)
    private Time weekendEndTime;

    public Store getStoreEntity(Member signUpMemberEntity) {
        return Store.builder()
                .memberNo(signUpMemberEntity)
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
