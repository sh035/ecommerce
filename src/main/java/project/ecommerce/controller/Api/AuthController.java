package project.ecommerce.controller.Api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ecommerce.domain.entity.Member;
import project.ecommerce.dto.MemberDto;
import project.ecommerce.dto.TokenDto;
import project.ecommerce.dto.TokenRequestDto;
import project.ecommerce.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@RequestBody MemberDto.SignupDto signupDto) {
        return ResponseEntity.ok(memberService.signup(signupDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberDto.LoginDto loginDto) {
        log.info("로그인 시도");
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }
}
