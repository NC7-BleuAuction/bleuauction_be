package bleuauction.bleuauction_be.server.member.controller;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.RefreshTokenRequest;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.member.dto.LoginRequest;
import bleuauction.bleuauction_be.server.member.dto.LoginResponseDto;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final CreateJwt createJwt;
    private final MemberService memberService;
    private final NcpObjectStorageService ncpObjectStorageService;
    private final AttachService attachService;

    /**
     * 관리자 페이지로 인하여 추가한 것으로 보임, 페이지네이션 추가 <br />
     * [TODO] : 관리자나 조회 가능한 사용자인지에 대한 인증 인가에 대한 처리를 추가해야 할 필요가 있어보임.
     *
     * @param page  PageNumber
     * @param limit 한번에 몇개의 사이즈를 가져갈지 지정
     * @return
     * @throws Exception
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> fineMemberList(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) throws Exception {
        return ResponseEntity.ok().body(
                memberService.findAllMemberByPageableOrderByRegDateDesc(page, limit)
        );
    }

    /**
     * MemberNo를 받아 해당 사용자의 정보 단건 조회, <br />
     * 사용자가 존재하지 않는 경우 MemberNotFoundException이 발생한다.
     *
     * @param memberNo
     * @return
     * @throws Exception
     */
    @GetMapping("/{memberNo}")
    public ResponseEntity<Member> fileMemberDetail(@PathVariable Long memberNo) throws Exception {
        return ResponseEntity.ok(memberService.findMemberById(memberNo));
    }

    /**
     * 관리자 페이지로 인하여 추가한 것으로 보임, 페이지네이션 추가. <br />
     * [TODO] : 관리자나 조회 가능한 사용자인지에 대한 인증 인가에 대한 처리를 추가해야 할 필요가 있어보임.
     *
     * @param memberNo
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{memberNo}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long memberNo) throws Exception {
        memberService.deleteMemberById(memberNo);
        return ResponseEntity.ok("Member Delete Success");
    }


    /**
     * 일반 사용자 회원가입.<br />
     * [TODO] : 추후 RequestDto 따로 제작해서 Entity받는게 아니라 RequestDto받도록 Refactoring 필요
     *
     * @param member
     * @return
     * @throws Exception
     */
    @PostMapping("/signup")
    public Member signUp(Member member) throws Exception {
        return memberService.signUp(member);
    }

    /**
     * 로그인 처리로직
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest loginRequest) {
        log.info("[MemberController] LoginRequest, Email >>> {}, Request Time >>> {}", loginRequest.getMemberEmail(), LocalDateTime.now());
        return ResponseEntity.ok(memberService.login(loginRequest.getMemberEmail(), loginRequest.getMemberPwd()));
    }

    /**
     * Refresh Token을 바탕으로 AccessToken 갱신 <br />
     *
     * @param refreshTokenRequest
     * @return
     */
    @PostMapping("/accTokRefresh")
    public ResponseEntity<?> refreshAccessToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        log.info("[MemberController] RefreshAccessToken, RefreshTokenValue >>> {}", refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", createJwt.getRenewAccessToken(refreshToken)));
    }

    /**
     * 인자값의 경우에는 건드리지 못하겠슴, 사용자 정보 수정
     * @param authorizationHeader 로그인한 사용자의 JWT토큰
     * @param updateMemberRequest
     * @return
     * @throws Exception
     */
    @PutMapping
    public ResponseEntity<String> updateMember(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateMemberRequest updateMemberRequest) throws Exception {
        createJwt.verifyAccessToken(authorizationHeader);

        // JWT토큰의 정보 추출
        TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

        //Profile Image ObjectStorage에 저장
        Attach attach = null;
        if(!updateMemberRequest.getProfileImage().isEmpty()){
            attach = ncpObjectStorageService.uploadFile("bleuauction-bucket", "member/", updateMemberRequest.getProfileImage());
        }

        //회원정보 수정, Attach Table 파일 추가
        Member updateMember = memberService.updateMember(tokenMember, updateMemberRequest, attach);
        attachService.addAttachs(updateMember.getMemberAttaches());
        return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
    }

    /**
     * 사용자 회원 탈퇴 <br />
     *
     * @param authorizationHeader
     * @return
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawMember(@RequestHeader("Authorization") String authorizationHeader) {
        createJwt.verifyAccessToken(authorizationHeader);
        //회원 탈퇴 처리 로직
        try {
            memberService.withDrawMember(createJwt.getTokenMember(authorizationHeader));
            return ResponseEntity.ok("회원이 성공적으로 탈퇴되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 탈퇴 중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원의 프로필 이미지를 삭제하는 기능으로 <br />
     * 해당 기능은 Controller가 적합함. <br />
     * [TODO] : 현재 해당 기능의 문제점은 인증 인가없이 그냥 fileNo를 입력할때 삭제가 된다는 점, 그러므로 타인이 삭제시킬수도 있음. 추후 보완이 필요하다.
     *
     * @param fileNo
     * @return
     */
    @DeleteMapping("/profileImage/{fileNo}")
    public ResponseEntity<String> deleteProfileImage(@PathVariable Long fileNo) {
        if (FileStatus.N.equals(attachService.changeFileStatusToDeleteByFileNo(fileNo).getFileStatus())) {
            return ResponseEntity.ok("Profile Image Delete Success");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Profile Image Delete Failed");
    }
}
