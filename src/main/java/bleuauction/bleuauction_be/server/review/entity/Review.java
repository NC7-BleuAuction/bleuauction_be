package bleuauction.bleuauction_be.server.review.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewNo;

  private Long storeNo;

  @JsonBackReference
  @JsonManagedReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="member_no")
  private Member member;

  private String reviewContent;

  private String reviewFreshness;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private ReviewStatus reviewStatus;

  @JsonManagedReference
  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Attach> reviewAttaches;

}
