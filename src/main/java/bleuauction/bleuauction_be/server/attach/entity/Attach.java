package bleuauction.bleuauction_be.server.attach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(name = "ba_attach")
@NoArgsConstructor
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    private Long fileNo;

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
    private AttachStatus attachStatus;
}
