package bleuauction.bleuauction_be.server.attach.entity;

import static jakarta.persistence.DiscriminatorType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.common.entity.AbstractTimeStamp;
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
import org.jetbrains.annotations.NotNull;

/** JOINED전략을 사용할지 SINGLE_TABLE 전략을 사용할지 고민했으나, 굳이 더 컬럼이 늘진 않을것 같아 SINGLE_TABLE 전략을 사용했습니다. */
@Entity
@Getter
@Setter
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "attach_type", discriminatorType = STRING)
@Table(name = "ba_attach")
@NoArgsConstructor(access = PROTECTED)
public abstract class Attach extends AbstractTimeStamp {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_no")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;

    @NotNull private String filePath;

    @NotNull private String originFilename;

    @NotNull private String saveFilename;

    protected Attach(AttachVO attachVO) {
        this.filePath = attachVO.getFilePath();
        this.originFilename = attachVO.getOriginFilename();
        this.saveFilename = attachVO.getSaveFileName();
        this.fileStatus = attachVO.getFileStatus();
    }

    // 비즈니스 로직
    public void deleteAttach() {
        this.fileStatus = FileStatus.N;
    }

    public void useAttach() {
        this.fileStatus = FileStatus.Y;
    }
}
