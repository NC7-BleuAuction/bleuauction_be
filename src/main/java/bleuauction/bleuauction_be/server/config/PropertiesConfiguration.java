package bleuauction.bleuauction_be.server.config;


import bleuauction.bleuauction_be.server.config.properties.NcpConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NcpConfigProperties.class})
public class PropertiesConfiguration {}
