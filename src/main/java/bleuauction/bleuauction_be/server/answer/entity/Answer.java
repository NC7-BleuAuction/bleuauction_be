package bleuauction.bleuauction_be.server.answer.entity;

import bleuauction.bleuauction_be.server.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@DynamicInsert
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ba_answer")
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long answerNo;

  private Long reviewNo;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name ="member_no")
  private Member member;

  private String answerContent;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private AnswerStatus answerStatus;

  @Builder
  public Answer(Long reviewNo, Member member, String answerContent,  AnswerStatus answerStatus) {
    this.reviewNo = reviewNo;
    this.member = member;
    this.answerContent = answerContent;
    this.answerStatus = answerStatus;
  }
}
