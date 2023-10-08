package bleuauction.bleuauction_be.server.attach.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttachStatus {
    Y("사용중"),
    N("삭제건");

private final String value;
}
