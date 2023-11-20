package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * TODO : 추후 menuNo, memberNo, review, noticeNo, storeNo의 관계 재정립 필요할 것으로 보임..
 * JSON으로 써야하는지도 확인해볼 사항인듯..?
 *
 */
@Entity
@Getter
@Setter
@Table(name = "ba_attach")
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    private Long fileNo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuNo")
    private Menu menuNo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member memberNo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewNo")
    private Review review;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeNo")
    private Notice noticeNo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeNo")
    private Store storeNo;

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

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;


    public void changeFileStatusToDelete() {
        this.fileStatus = FileStatus.N;
    }

    public void changeFileStatusToUsecase() {
        this.fileStatus = FileStatus.Y;
    }
}
