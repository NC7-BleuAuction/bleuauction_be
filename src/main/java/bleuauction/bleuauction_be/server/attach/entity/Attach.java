package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.item.entity.Item;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.review.entity.Review;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ba_attach")
@NoArgsConstructor
@DynamicInsert
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    private Long fileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuNo")
    private Menu menuNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewNo")
    private Review reviewNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemNo")
    private Item itemNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeNo")
    private Notice noticeNo;


    @NotNull
    private String filePath;

    @NotNull
    private String originFilename;

    @NotNull
    private String saveFilename;

    @CurrentTimestamp
    @Column(name = "reg_datetime")
    private Timestamp regDatetime;

    @UpdateTimestamp
    @Column(name = "mdf_datetime")
    private Timestamp mdfDatetime;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;


}
