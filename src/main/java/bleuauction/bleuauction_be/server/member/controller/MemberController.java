package bleuauction.bleuauction_be.server.member.controller;

import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.member.service.UpdateMemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("{memberNo}")
    public ResponseEntity<Object> detail(@PathVariable Long memberNo) throws Exception {
        Optional<Member> memberOptional = memberRepository.findById(memberNo);
                if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return ResponseEntity.ok().body(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Member>> list () throws Exception {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok().body(members);
    }

    @GetMapping("/form")
    public ResponseEntity<Map<String, String>> form(@CookieValue(required = false) String memberEmail) {
        Map<String, String> response = new HashMap<>();
        response.put("memberEmail", memberEmail);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/delete")
    public void delete(Long memberNo) throws Exception {
        memberRepository.findById(memberNo).ifPresentOrElse(
                memberRepository::delete,
                () -> new MemberNotFoundException("해당 번호의 회원이 없습니다."));
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) throws Exception {
        session.invalidate();
        log.info("Call logout");
        return ResponseEntity.ok().body("{\"message\": \"Logout successful\"}");
    }
    // 일반사용자 회원가입
    @PostMapping("/signup")
    public Member signUp(@RequestBody Member member) throws Exception {
        log.error("Email:[{}], Password:[{}]", member.getMemberEmail(), member.getMemberPwd());
        // 비밀번호를 암호화하여 저장
        String encryptedPassword = passwordEncoder.encode(member.getMemberPwd());
        member.setMemberPwd(encryptedPassword);
        // 회원 저장
        return memberRepository.save(member);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest,
            HttpSession session,
            HttpServletResponse response) throws Exception {
        String memberEmail = loginRequest.get("memberEmail");
        String memberPwd = loginRequest.get("memberPwd");
        Map<String, Object> responseMap = new HashMap<>();

        if (memberEmail != null && !memberEmail.isEmpty()) {  // null 또는 비어 있는지 확인
            Cookie cookie = new Cookie("memberEmail", memberEmail);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("memberEmail", "");  // 빈 문자열로 설정
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        Member loginUser = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다."));
        log.error("사용자 정보는 있나? >>> {}", loginUser.toString());
        log.error("동일 한가 ? >>>{}}", passwordEncoder.matches(memberPwd, loginUser.getMemberPwd()));
        if (!passwordEncoder.matches(memberPwd, loginUser.getMemberPwd())) {
            responseMap.put("message", "회원 정보가 일치하지 않습니다.");
            responseMap.put("status", "error");
            return ResponseEntity.status(400).body(responseMap);
        }

        session.setAttribute("loginUser", loginUser);
        responseMap.put("message", "login success");
        log.info(session.getAttribute("loginUser") + "");
        responseMap.put("status", "success");
        log.info("Call login");
        return ResponseEntity.ok(responseMap);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateMember(HttpSession session, @Valid @RequestBody UpdateMemberRequest updateMemberRequest) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        try {
            UpdateMemberService service = new UpdateMemberService(memberRepository);
            service.updateMember(loginUser.getMemberNo(), updateMemberRequest);
            log.info("회원 정보가 업데이트되었습니다. 업데이트된 회원 정보: {}", updateMemberRequest);
            return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾을 수 없습니다.");
        }
    }
}
