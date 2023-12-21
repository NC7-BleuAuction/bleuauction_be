package bleuauction.bleuauction_be.server.store.util;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;

import java.sql.Time;
import java.time.LocalTime;

public class StoreUtilFactory {

    public static Store of(String marketName, String storeName, String licenseNo) {
        return of(MemberEntityFactory.mockSellerMember, marketName, storeName, licenseNo);
    }

    public static Store of(Member member, String marketName, String storeName, String licenseNo) {
        return Store.builder()
                .member(member)
                .marketName(marketName)
                .storeName(storeName)
                .licenseNo(licenseNo)
                .storeZipcode("14011")
                .storeAddr("대한민국 어딘가")
                .storeDetailAddr("강남의 달래해장건물 5층?")
                .weekdayStartTime(Time.valueOf(LocalTime.of(9, 0, 0)))
                .weekdayEndTime(Time.valueOf(LocalTime.of(23, 0, 0)))
                .weekendStartTime(Time.valueOf(LocalTime.of(11, 0, 0)))
                .weekendEndTime(Time.valueOf(LocalTime.of(23,59,59)))
                .storeStatus(StoreStatus.Y)
                .build();
    }

}
