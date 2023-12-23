package bleuauction.bleuauction_be.server.review.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewStatus {
    Y("사용"),
    N("삭제");

    private final String value;
}
