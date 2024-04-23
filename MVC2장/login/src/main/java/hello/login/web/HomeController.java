package hello.login.web;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
    // @GetMapping("/")
    public String home() {
        return "home";
    }
    //@GetMapping("/")
    public String homeLogin(
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model) {
        if (memberId == null) {
            return "home";
        }
    //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        // 세션 관리자에 저장된 회원 정보 조회
        Object member = (Member) sessionManager.getSession(request);
        //로그인

        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }
}