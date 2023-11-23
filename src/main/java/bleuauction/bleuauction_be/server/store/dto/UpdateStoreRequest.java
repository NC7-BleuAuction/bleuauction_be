package bleuauction.bleuauction_be.server.store.dto;

import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Time;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateStoreRequest {

    private MemberCategory memberCategory = MemberCategory.S;
    private TokenMember tokenMember;

    private Long memberNo;

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

    @NotNull(message = "시장명은 필수 입력값입니다.")
    @Size(min = 2, max = 20)
    private String marketName;

    @NotNull(message = "가게명은 필수 입력값입니다.")
    @Size(min = 2, max = 20)
    private String storeName;

    @NotNull(message = "사업자등록번호는 필수 입력값입니다.")
    private String licenseNo;

    @NotNull(message = "가게우편번호는 필수 입력값입니다.")
    private String storeZipcode;

    @NotNull(message = "가게주소는 필수 입력값입니다.")
    private String storeAddr;

    @NotNull(message = "가게상세주소는 필수 입력값입니다.")
    private String storeDetailAddr;

    private Time weekdayStartTime;

    private Time weekdayEndTime;

    private Time weekendStartTime;

    private Time weekendEndTime;

    // TODO : 주문 불가 유형 넣을지 생각해보기

    private MultipartFile profileImage;

}
