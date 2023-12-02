package bleuauction.bleuauction_be.server.config;

import bleuauction.bleuauction_be.server.properties.IamportProperties;
import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PayConfig {

    private final IamportProperties iamportProperties;

    @Bean
    public IamportClient iamportClientBean() {
        return new IamportClient(iamportProperties.getKey(), iamportProperties.getSecret());
    }
}
