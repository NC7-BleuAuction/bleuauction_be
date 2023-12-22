package bleuauction.bleuauction_be.server.attach.util;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.AttachVO;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AttachUtilFactory {

    /**
     * FileStatus가 Y인 객체 생성 공장
     *
     * @param filePath
     * @param originalFileName
     * @param saveFileName
     * @return
     */
    public static Attach ofUseCase(String filePath, String originalFileName, String saveFileName) {
        Attach attach = ofDefaultAttach(filePath, originalFileName, saveFileName);
        attach.useAttach();
        return attach;
    }

    /**
     * FileStatus가 N인 객체 생성 공장
     *
     * @param filePath
     * @param originalFileName
     * @param saveFileName
     * @return
     */
    public static Attach ofDeleteCase(
            String filePath, String originalFileName, String saveFileName) {
        Attach attach = ofDefaultAttach(filePath, originalFileName, saveFileName);
        attach.deleteAttach();
        return attach;
    }

    /**
     * 기본 공통사항을 구성을 생성하는 Attach Method
     *
     * @param filePath
     * @param originalFileName
     * @param saveFileName
     * @return
     */
    private static Attach ofDefaultAttach(
            String filePath, String originalFileName, String saveFileName) {
        Attach attach =
                AttachVO.builder()
                        .filePath(filePath)
                        .originFilename(originalFileName)
                        .saveFileName(saveFileName)
                        .build();
        attach.setRegDatetime(Timestamp.valueOf(LocalDateTime.now()));
        attach.setMdfDatetime(Timestamp.valueOf(LocalDateTime.now()));
        return attach;
    }
}
