package bleuauction.bleuauction_be.server.notice.dto;


import jakarta.persistence.Lob;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

    private String noticeTitle;
    private String noticeContent;

}
