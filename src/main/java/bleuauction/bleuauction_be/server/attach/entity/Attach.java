package bleuauction.bleuauction_be.server.attach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

/**
 * JOINED전략을 사용할지 SINGLE_TABLE 전략을 사용할지 고민했으나, 굳이 더 컬럼이 늘진 않을것 같아 SINGLE_TABLE 전략을 사용했습니다.
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "attach_type")
@Table(name = "ba_attach")
@NoArgsConstructor(access = PROTECTED)
public abstract class Attach {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_no")
    private Long id;

    @Enumerated(STRING)
    private FileStatus fileStatus;

    @NotNull
    private String filePath;

    @NotNull
    private String originFilename;

    @NotNull
    private String saveFilename;

    @CreationTimestamp
    private Timestamp regDatetime;

    @UpdateTimestamp
    private Timestamp mdfDatetime;

    protected Attach(AttachVO attachVO) {
        this.filePath = attachVO.getFilePath();
        this.originFilename = attachVO.getOriginFilename();
        this.saveFilename = attachVO.getSaveFileName();
        this.fileStatus = attachVO.getFileStatus();
    }

    public void changeFileStatusToDelete() {
        this.fileStatus = FileStatus.N;
    }

    public void changeFileStatusToUsecase() {
        this.fileStatus = FileStatus.Y;
    }
}
