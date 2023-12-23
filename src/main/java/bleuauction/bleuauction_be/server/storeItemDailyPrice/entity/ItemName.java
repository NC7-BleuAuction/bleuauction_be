package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemName {
    FI("광어"), // Flounder
    SB("우럭"), // Sea Bass
    FT("도미"), // Flatfish
    MB("방어"), // Mackerel
    TT("참치"), // Tuna
    SL("연어"), // Salmon
    SQ("오징어"), // Squid
    SH("새우"), // Shrimp
    SC("가리비"), // Scallop
    AB("전복"), // Abalone
    HT("고등어"); // Hairtail

    private final String value;
}
