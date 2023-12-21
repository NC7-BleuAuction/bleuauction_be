package bleuauction.bleuauction_be.server.review.exception;

public class ReviewNotFoundException extends RuntimeException {

  private static final String MESSAGE = "[ReviewNotFoundException] Not Found Review";

  public ReviewNotFoundException() {
    super();
  }

  public ReviewNotFoundException(String message) {
    super(message);
  }

  public ReviewNotFoundException(Long ReviewNo) {
    super(String.format(MESSAGE + ", Request Review No >>> %d", ReviewNo));
  }

}
