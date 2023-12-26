package bleuauction.bleuauction_be.server.config.bean.ncp;


import bleuauction.bleuauction_be.server.config.properties.NcpConfigProperties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NcpObjectStorageBean {
    private final NcpConfigProperties ncpConfigProperties;

    @Bean(name = "ncpObjectStorageS3")
    public AmazonS3 storageObject() {
        log.info("Create Naver ObjectStorageConfig Bean");
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(this.getEndpointConfig())
                .withCredentials(this.getCredentialsProvier())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfig() {
        return new AwsClientBuilder.EndpointConfiguration(
                this.ncpConfigProperties.getEndPoint(), this.ncpConfigProperties.getRegionName());
    }

    private AWSStaticCredentialsProvider getCredentialsProvier() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        ncpConfigProperties.getAccessKey(), ncpConfigProperties.getSecretKey()));
    }
}
