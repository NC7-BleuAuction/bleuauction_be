package bleuauction.bleuauction_be.server.util;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TokenMember {
  private Long memberNo;

  private String memberEmail;

  private String memberName;


}

