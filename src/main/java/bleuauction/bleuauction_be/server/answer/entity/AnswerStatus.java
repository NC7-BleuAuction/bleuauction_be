package bleuauction.bleuauction_be.server.answer.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnswerStatus {
    Y("사용중"),
    N("삭제건");
    private final String value;
}
