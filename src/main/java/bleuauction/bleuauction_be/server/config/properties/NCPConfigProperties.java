package bleuauction.bleuauction_be.server.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "ncp")
public class NCPConfigProperties {
  // ncp.properties 파일의 속성 값을 자동으로 지정하기
  // => @ConfigurationProperties("접두사") 애노테이션을 사용하여 가져올 프로퍼티를 지정한다.
  // => 단 접두사를 제외한 프로퍼티 이름과 자바 객체의 필드 이름이 일치해야 한다.
  private String endPoint;
  private String regionName;
  private String accessKey;
  private String secretKey;
  private String bucketName;
}