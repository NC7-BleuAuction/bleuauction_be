package bleuauction.bleuauction_be.server.store.exception;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.store.entity.Store;

import java.time.LocalDateTime;

public class StoreUpdateUnAuthorizedException extends RuntimeException{
    private static final String MESSAGE = "[StoreUpdateUnAuthorizedException] UnAuthorized ";

    public StoreUpdateUnAuthorizedException() {
        super();
    }

    public StoreUpdateUnAuthorizedException(Store store, Member requestUser) {
        super(
                String.format(
                        MESSAGE+"WithDraw, requestUser >>> [ID : %d, Email : %s], Store >>> [ID : %d, StoreName : %s], exceptionTime : %s",
                        requestUser.getMemberNo(), requestUser.getMemberEmail(),
                        store.getStoreNo(), store.getStoreName(),
                        LocalDateTime.now()
                )

        );
    }
    public StoreUpdateUnAuthorizedException(Member requestUser, Member targetUser) {
        super(
                String.format(
                        MESSAGE+"UpdateUser, requestUser >>> [ID : %d, Email : %s], targetUser >>> [ID : %d, Email : %s], exceptionTime : %s",
                        requestUser.getMemberNo(), requestUser.getMemberEmail(),
                        targetUser.getMemberNo(), targetUser.getMemberEmail(),
                        LocalDateTime.now()
                )

        );
    }
}
