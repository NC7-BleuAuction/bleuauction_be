package bleuauction.bleuauction_be.server.review.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
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

  @JsonIgnore
  @OneToMany(mappedBy = "reviewNo", cascade=CascadeType.ALL)
  private List<Attach> reivewAttaches;
}
