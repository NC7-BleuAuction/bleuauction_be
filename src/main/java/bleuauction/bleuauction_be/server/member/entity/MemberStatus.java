package bleuauction.bleuauction_be.server.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
  Y("일반회원"),
  N("탈퇴회원");

  private final String value;
}

