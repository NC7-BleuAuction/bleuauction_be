package bleuauction.bleuauction_be.server.store.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {
    Y("운영중"),
    N("폐업");

    private final String value;
}
