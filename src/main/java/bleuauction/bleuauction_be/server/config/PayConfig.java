package bleuauction.bleuauction_be.server.config;

import com.siot.IamportRestClient.IamportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PayConfig {

    @Bean
    public IamportClient iamportClient(@Value("${iamport.key}") String restApiKey,
            @Value("${iamport.secret}") String restApiSecret) {
        return new IamportClient(restApiKey, restApiSecret);
    }
}
