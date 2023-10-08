package bleuauction.bleuauction_be.server.review.entity;

import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.entity.UnsupportedType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewNo;

  private Long storeNo;

  private Long memberNo;

  private String reviewContent;

  private String reviewFreshness;

  private Timestamp regDatetime;

  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private ReviewStatus reviewStatus;
}
