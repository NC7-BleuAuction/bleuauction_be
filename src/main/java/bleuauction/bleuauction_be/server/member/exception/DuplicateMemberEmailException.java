package bleuauction.bleuauction_be.server.member.exception;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DuplicateMemberEmailException extends RuntimeException {
    private static final String MESSAGE =
            "[DuplicateMemberEmailException] Duplicate Member Email, requestEmail >>> ";

    public DuplicateMemberEmailException(String email) {
        super(String.format(MESSAGE + "%s", email));
        log.warn(MESSAGE + "{}", email);
    }
}
