package bleuauction.bleuauction_be.server.pay.exception;

import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class PayException {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
            String message = "값이 제대로 입력되지 않았습니다. (DataIntegrityViolationException)";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    // TODO : 추후 아래 Exception 활성화 예정

//        @ExceptionHandler(verifyIamportException.class)
//        public ResponseEntity<String> handleVerifyIamportException(verifyIamportException e) {
//            return new ResponseEntity<>("실제 결제금액과 서버에서 결제금액이 다릅니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        @ExceptionHandler(RefundAmountIsDifferent.class)
//        public ResponseEntity<String> handleRefundAmountIsDifferent(RefundAmountIsDifferent e) {
//            return new ResponseEntity<>("환불가능 금액과 결제했던 금액이 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }

        @ExceptionHandler(IamportResponseException.class)
        public ResponseEntity<String> handleIamportResponseException(IamportResponseException e) {
            return new ResponseEntity<>("결제관련해서 에러가 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception e) {
            String message = "서버 에러 입니다. (Exception)";
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
