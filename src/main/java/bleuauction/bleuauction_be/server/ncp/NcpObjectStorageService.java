package bleuauction.bleuauction_be.server.ncp;


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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
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
    if (part.isEmpty()) {
      return null; // 업로드된 파일이 없으면 null 반환
    }

    try {
      String originFileName = part.getOriginalFilename();
      String saveFileName = UUID.randomUUID().toString();
      attach.setOriginFilename(originFileName);
      attach.setSaveFilename(saveFileName);
      attach.setFilePath(dirPath);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(part.getContentType());

      // Amazon S3에 파일 업로드
      s3.putObject(new PutObjectRequest(bucketName, dirPath + saveFileName, part.getInputStream(), objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));

      return attach;
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 오류", e);
    }
  }
}
