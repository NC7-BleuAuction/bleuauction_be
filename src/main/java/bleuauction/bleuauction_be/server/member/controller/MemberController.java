package bleuauction.bleuauction_be.server.member.controller;

import bleuauction.bleuauction_be.server.member.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/member/signup")
    public String signUpForm() {
        return "member/signup";
    }

    // 일반사용자 회원가입
    @PostMapping("/member/signup")
    public Member signUp(@RequestBody Member member) throws Exception {
        log.error("Email:[{}], Password:[{}]", member.getMemberEmail(), member.getMemberPwd());
        // 비밀번호를 암호화하여 저장
        String encryptedPassword = passwordEncoder.encode(member.getMemberPwd());
        member.setMemberPwd(encryptedPassword);
        // 회원 저장
        return memberRepository.save(member);
    }
    // 가게 회원가입
    @PostMapping("/store/signup")
    public Store storeSignUp(@RequestBody @Valid StoreSignUpRequest request) throws Exception {
        String memberEmail = request.getMemberEmail();
        String memberPwd = request.getMemberPwd();
        log.error("Email:[{}], Password:[{}]", memberEmail, memberPwd);

        //회원가입
        Member member = memberService.signUp(request.getMemberEntity());
        Store store = request.getStoreEntity(member);
        return storeRepository.save(store);
    }

    @GetMapping("/member/delete")
    public void delete(Long memberNo) throws Exception {
        memberRepository.findById(memberNo).ifPresentOrElse(
                memberRepository::delete,
                () -> new MemberNotFoundException("해당 번호의 회원이 없습니다."));
    }

    @GetMapping("{memberNo}")
    public String detail(@PathVariable Long memberNo, Model model) throws Exception {
        model.addAttribute("member", memberRepository.findById(memberNo));
        return "member/detail";
    }

    @GetMapping("/member/list")
    public void list(Model model) throws Exception {
        model.addAttribute("member/list", memberRepository.findAll());
    }

    @GetMapping("/member/form")
    public String form(@CookieValue(required = false) String memberEmail, Model model) {
        model.addAttribute("memberEmail", memberEmail);
        return "member/form";
    }

    @PostMapping("/member/login")
    public String login(
            @RequestParam("memberEmail") String memberEmail,
            @RequestParam("memberPwd") String memberPwd,
            HttpSession session,
            Model model,
            HttpServletResponse response) throws Exception {
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
            model.addAttribute("refresh", "2;url=/member/form");
            throw new Exception("회원 정보가 일치하지 않습니다.");
        }

        session.setAttribute("loginUser", loginUser);
        log.info("Call login");
        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) throws Exception {
        session.invalidate();
        log.info("Call logout");
        return "redirect:/";
    }
}
