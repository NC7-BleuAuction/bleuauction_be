package bleuauction.bleuauction_be.server.config;

import bleuauction.bleuauction_be.server.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(
        {
                CorsProperties.class,
        }
)
@Configuration
public class ConfigurationProperties {

}
