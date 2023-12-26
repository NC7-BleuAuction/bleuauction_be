package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OriginStatus {
    D("국내산"),
    I("수입산");

    private final String value;
}
