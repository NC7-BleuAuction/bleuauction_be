package bleuauction.bleuauction_be.server.member.entity;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 주소 정보 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class Address {
    @NotNull private String zipCode;

    @NotNull private String addr;

    @NotNull private String detailAddr;
}
