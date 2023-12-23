package bleuauction.bleuauction_be.server.review.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewFreshness {
    L("낮음"),
    M("중간"),
    H("높음");

    private final String value;
}
