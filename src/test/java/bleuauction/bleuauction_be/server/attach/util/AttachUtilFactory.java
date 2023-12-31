package bleuauction.bleuauction_be.server.attach.util;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.AttachVO;
import bleuauction.bleuauction_be.server.attach.entity.MemberAttach;

public class AttachUtilFactory {

    // TODO : 일단 실행이 1순위니까 돌아가게만 하기위해 MemberAttach로 다 때려박음

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
        return new MemberAttach(
                AttachVO.builder()
                        .filePath(filePath)
                        .originFilename(originalFileName)
                        .saveFileName(saveFileName)
                        .build());
    }
}
