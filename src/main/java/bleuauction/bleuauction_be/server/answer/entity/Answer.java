package bleuauction.bleuauction_be.server.answer.entity;

import bleuauction.bleuauction_be.server.review.entity.ReviewStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_answer")
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long answerNo;

  private Long reviewNo;

  private Long memberNo;

  private String answerContent;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private AnswerStatus answerStatus;
}
