package bleuauction.bleuauction_be.server.member.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberNotFoundException extends RuntimeException {
    private static final String DEFAULT_CONSTRUCTOR_MESSAGE =
            "[MemberNotFoundException] Not Found Member";
    private static final String MESSAGE = DEFAULT_CONSTRUCTOR_MESSAGE + ", requestMemberNo >>> ";

    public MemberNotFoundException() {
        super(DEFAULT_CONSTRUCTOR_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(DEFAULT_CONSTRUCTOR_MESSAGE + " " + message);
    }

    public MemberNotFoundException(Long memberNo) {
        super(String.format(MESSAGE + "%d", memberNo));
        log.warn(MESSAGE + "{}", memberNo);
    }
}
