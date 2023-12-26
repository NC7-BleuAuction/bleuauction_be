package bleuauction.bleuauction_be.server.properties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "iamport")
public class IamportProperties {
    private String key;
    private String secret;
}
