package bleuauction.bleuauction_be.server.store.exception;

import bleuauction.bleuauction_be.server.member.entity.Member;

import java.time.LocalDateTime;

public class StoreRequestUnAuthorizationException extends RuntimeException {
    private static final String MESSAGE = "[StoreRequestUnAuthorizationException] UnAuthorized ";

    public StoreRequestUnAuthorizationException() {
        super();
    }

    public StoreRequestUnAuthorizationException(Member requestMember) {
        super(
                String.format(
                        MESSAGE+"UpdateUser, requestUser >>> [ID : %d, Email : %s], exceptionTime : %s",
                        requestMember.getId(), requestMember.getEmail(), LocalDateTime.now()
                )
        );
    }
}
