package bleuauction.bleuauction_be.server.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({CorsProperties.class, IamportProperties.class})
@Configuration
public class ConfigurationProperties {}
