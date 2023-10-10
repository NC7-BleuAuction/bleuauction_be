package bleuauction.bleuauction_be.server;



import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.config.NcpConfig;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
public class NcpObjectStorageService {

  final AmazonS3 s3;

  public NcpObjectStorageService(NcpConfig ncpConfig) {
    s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                    ncpConfig.getEndPoint(), ncpConfig.getRegionName()))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                    ncpConfig.getAccessKey(), ncpConfig.getSecretKey())))
            .build();
  }
  public Attach uploadFile(Attach attach, String bucketName, String dirPath, MultipartFile part) {
    if (part.getSize() == 0) {
      return null;
    }

    try (InputStream fileIn = part.getInputStream()) {
      String originFileName = part.getOriginalFilename();
      String saveFileName = UUID.randomUUID().toString();
      attach.setOriginFilename(originFileName);
      attach.setSaveFilename(saveFileName);
      attach.setFilePath(dirPath);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(part.getContentType());

      PutObjectRequest objectRequest = new PutObjectRequest(
              bucketName,
              dirPath + saveFileName,
              fileIn,
              objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

      s3.putObject(objectRequest);

      return attach;

    } catch (Exception e) {
      throw new RuntimeException("파일 업로드 오류", e);
    }
  }
}

