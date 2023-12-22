package bleuauction.bleuauction_be.server.attach.util;


import com.amazonaws.services.s3.model.ObjectMetadata;
import java.util.UUID;

public class AttachFileUtils {

    /**
     * 파일 이름을 랜덤으로 생성하여 반환
     *
     * @return
     */
    public static String getRandomNewFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 파일의 ObjectMetaData를 생성 및 반환
     *
     * @param contentType
     * @param bytes
     * @return
     */
    public static ObjectMetadata getObjectMetadata(String contentType, byte[] bytes) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(bytes.length);
        return metadata;
    }
}
