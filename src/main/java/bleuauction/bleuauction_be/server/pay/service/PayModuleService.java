package bleuauction.bleuauction_be.server.pay.service;

import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.exception.PayHistoryNotFoundException;
import bleuauction.bleuauction_be.server.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ModuleService
@RequiredArgsConstructor
@Transactional
public class PayModuleService {
    private final PayRepository payRepository;

    /**
     * 결제 정보를 조회하며,
     * 존재하지 않는 history인 경우에는 PayHistoryNotFoundException이 발생한다.
     * @param payNo
     * @return
     */
    public Pay findById(Long payNo) {
        return payRepository.findById(payNo)
                .orElseThrow(() -> new PayHistoryNotFoundException(payNo));
    }

    public Pay save(Pay pay) {
        return payRepository.save(pay);
    }
}
