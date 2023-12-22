package bleuauction.bleuauction_be.server.attach.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AttachVO {
    private String originFilename;
    private String saveFileName;
    private String filePath;
    private FileStatus fileStatus;
}
