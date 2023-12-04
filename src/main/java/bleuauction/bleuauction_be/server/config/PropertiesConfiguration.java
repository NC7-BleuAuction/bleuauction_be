package bleuauction.bleuauction_be.server.config;

import bleuauction.bleuauction_be.server.config.properties.NCPConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        NCPConfigProperties.class
})
public class PropertiesConfiguration {
}
