package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private String filePath;

    @NotNull
    private String originFilename;

    @NotNull
    private String saveFilename;

    @CurrentTimestamp
    @Column(name = "reg_datetime")
    private LocalDateTime regDatetime;

    @UpdateTimestamp
    @Column(name = "mdf_datetime")
    private LocalDateTime mdfDatetime;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;


}
