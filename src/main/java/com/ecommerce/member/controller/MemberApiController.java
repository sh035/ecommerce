package com.ecommerce.member.controller;

import com.ecommerce.global.security.auth.PrincipalDetails;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignInResponseDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid MemberSignupDto dto) {
        memberService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<MemberSignInResponseDto> signIn(@RequestBody @Valid MemberSignInRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
        .body(memberService.signIn(dto));
    }

    @PatchMapping("/update/auth")
    public ResponseEntity<String> updateOAuth2(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody @Valid MemberOAuthUpdateDto dto) {
        memberService.updatePhone(principalDetails.getName(), dto);
        return ResponseEntity.status(HttpStatus.OK)
            .body("전화번호가 등록되었습니다.");
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@AuthenticationPrincipal PrincipalDetails principalDetails
        , @RequestBody @Valid MemberUpdateDto dto) {
        memberService.update(principalDetails.getName(), dto);

        return ResponseEntity.status(HttpStatus.OK)
            .body("회원정보가 변경되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
        HttpServletRequest request) {
        memberService.logout(principalDetails.getName(), request);

        return ResponseEntity.status(HttpStatus.OK)
            .body("로그아웃 되었습니다.");
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody @Valid MemberWithdrawalDto dto) {
        memberService.withdrawal(principalDetails.getName(), dto);
        return ResponseEntity.status(HttpStatus.OK)
            .body("회원탈퇴 되었습니다.");
    }
}
