package bleuauction.bleuauction_be.server.member.entity;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 계좌 정보 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class BankAccount {
    @Nullable private String bankName;

    @Nullable private String bankAccount;
}
