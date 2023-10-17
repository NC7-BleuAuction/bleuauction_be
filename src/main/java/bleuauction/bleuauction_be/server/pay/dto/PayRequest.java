package bleuauction.bleuauction_be.server.pay.dto;

import bleuauction.bleuauction_be.server.pay.entity.Pay;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayRequest {

    private Long payNo;

    private List<PayRequest> payRequests;

}
