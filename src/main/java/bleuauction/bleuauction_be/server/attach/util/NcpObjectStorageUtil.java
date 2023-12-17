package bleuauction.bleuauction_be.server.attach.util;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.exception.AttachUploadBadRequestException;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.config.properties.NcpConfigProperties;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Configuration
public class NcpObjectStorageUtil {

  private final NcpConfigProperties ncpConfigProperties;
  private final Environment environment;
  private final AmazonS3 s3;

  public NcpObjectStorageUtil(
          NcpConfigProperties ncpConfigProperties,
          Environment environment,
          @Qualifier("ncpObjectStorageS3") AmazonS3 s3
  ) {
    this.ncpConfigProperties = ncpConfigProperties;
    this.environment = environment;
    this.s3 = s3;
  }

  /**
   * 메소드 내부에서 객체 생성후 반환
   * @param usage
   * @param part
   * @return
   */
  public Attach uploadFile(FileUploadUsage usage, MultipartFile part) {
    // 요청해야 할 파일이 없는 경우
    if (part==null || part.isEmpty()) {
      throw new AttachUploadBadRequestException();
    }

    try {
      String saveFileName = AttachFileUtils.getRandomNewFileName();
      String path = getPath(usage);

      this.uploadFileToObjectStorage(String.join("/", path, saveFileName), part);

      return Attach.builder()
              .originFilename(part.getOriginalFilename())
              .saveFilename(saveFileName)
              .filePath(path)
              .fileStatus(FileStatus.Y)
              .build();
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 오류", e);
    }
  }

  /**
   * Amazon S3에 파일 업로드
   * @param fileName
   * @param part
   * @throws IOException
   */
  //@Async
  public void uploadFileToObjectStorage(String fileName, MultipartFile part) throws IOException {
    s3.putObject(
            new PutObjectRequest(
                    ncpConfigProperties.getBucketName(),
                    fileName,
                    part.getInputStream(),
                    AttachFileUtils.getObjectMetadata(part.getContentType(), part.getBytes())
            )
    );
  }


  /**
   * ObjectStorage에 저장될 File Path
   * Example) active : "dev", userType : "article", fileName : "a.jpg", 23year10Month9Day
   * "dev/article/2023/10/09/a.jpg"
   *
   * @param usage
   * @return
   */
  private String getPath(FileUploadUsage usage) {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    return String.join("/",
            environment.getProperty("spring.profiles.active", "default"),
            usage.getPath() ,
            String.valueOf(now.getYear()),
            String.valueOf(now.getMonthValue()),
            String.valueOf(now.getDayOfMonth())
    );
  }

}
