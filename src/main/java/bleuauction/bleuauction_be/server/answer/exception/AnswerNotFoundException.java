package bleuauction.bleuauction_be.server.answer.exception;

public class AnswerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "[AnswerNotFoundException] Not Found Answer";

    public AnswerNotFoundException() {
        super();
    }

    public AnswerNotFoundException(String message) {
        super(message);
    }

    public AnswerNotFoundException(Long answerNo) {
        super(String.format(MESSAGE + ", Request Answer No >>> %d", answerNo));
    }
}
