package bleuauction.bleuauction_be.server.pay.controller;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.repository.PayRepository;
import bleuauction.bleuauction_be.server.util.CreateJwt;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/pay")
public class PayController {
    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;
    private final OrderService orderService;
    private final PayRepository payRepository;
    private IamportClient iamportClient;
    private final CreateJwt createJwt;

    public PayController(OrderService orderService, PayRepository payRepository, @Value("${iamport.key}") String restApiKey, @Value("${iamport.secret}") String restApiSecret,
            CreateJwt createJwt) {
        this.orderService = orderService;
        this.payRepository = payRepository;
        this.restApiKey = restApiKey;
        this.restApiSecret = restApiSecret;
        this.createJwt = createJwt;
        log.info("Rest API Key: {}, Rest API Secret: {}", restApiKey, restApiSecret);
    }

    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestHeader("Authorization") String authorizationHeader,
    @RequestBody PayInsertRequest payInsertRequest) {
        ResponseEntity<?> verificationResult = createJwt.verifyAccessToken(
                authorizationHeader,
                createJwt);
        if (verificationResult != null) {
            return verificationResult;
        }
        Long orderNo = payInsertRequest.getOrderNo();
        Optional<Order> orderUser = Optional.ofNullable(orderService.findOne(orderNo));

        try {
            Order order = orderService.findOne(payInsertRequest.getOrderNo());
//            Optional<Order> orderOptional = orderService.getOrderById(payInsertRequest.getOrderNo());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Pay pay = payInsertRequest.getPayEntity(order);
            Pay savedPay = payRepository.save(pay);

            return ResponseEntity.ok(savedPay);
        } catch (Exception e) {
            log.error("Error creating payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

        @GetMapping("/{payNo}")
        public ResponseEntity<Object> detail (@PathVariable Long payNo) throws Exception {
            Optional<Pay> payOptional = payRepository.findBypayNo(payNo);

            if (payOptional.isPresent()) {

                Pay pay = payOptional.get();
                return ResponseEntity.ok().body(pay);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

        @PostMapping("/verifyIamport/{imp_uid}")
        public IamportResponse<Payment> paymentByImpUid (@PathVariable("imp_uid") String imp_uid) throws
        IamportResponseException, IOException {
            return iamportClient.paymentByImpUid(imp_uid);
        }
}
