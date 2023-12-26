package bleuauction.bleuauction_be.server.attach.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileUploadUsage {
    MEMBER("member"),
    MENU("menu"),
    STORE("store"),
    NOTICE("notice"),
    REVIEW("review");

    private String path;
}
