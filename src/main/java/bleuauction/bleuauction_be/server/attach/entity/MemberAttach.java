package bleuauction.bleuauction_be.server.attach.entity;

import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.member.entity.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("MEMBER")
public class MemberAttach extends Attach {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    public MemberAttach(AttachVO attachVO) {
        super(attachVO);
    }

    public MemberAttach(AttachVO attachVO, Member member) {
        this(attachVO);
        setMember(member);
    }

    // 파일추가 및 회원 추가
    public void setMember(Member member) {
        this.member = member;
        member.getAttaches().add(this);
    }
}
