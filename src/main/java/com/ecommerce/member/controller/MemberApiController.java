package com.ecommerce.member.controller;

import com.ecommerce.global.security.auth.PrincipalDetails;
import com.ecommerce.member.domain.dto.MemberConfirmEmailDto;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberApiController {
  private final MemberService memberService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody MemberSignupDto dto) {
    return ResponseEntity.ok(memberService.signUp(dto));
  }

  @PostMapping("/email/confirm")
  public ResponseEntity<?> confirmEmail(@RequestBody MemberConfirmEmailDto dto) {
    return ResponseEntity.ok(memberService.confirmEmail(dto));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@RequestBody MemberSignInRequestDto dto) {
    return ResponseEntity.ok(memberService.signIn(dto));
  }

  @PutMapping("/update/OAuth2")
  public ResponseEntity<?> updateOAuth2(@AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestBody MemberOAuthUpdateDto dto) {
    return ResponseEntity.ok(memberService.updatePhone(principalDetails.getMember(), dto));
  }

  @PutMapping("/update")
  public ResponseEntity<?> update(@AuthenticationPrincipal PrincipalDetails principalDetails
      , @RequestBody MemberUpdateDto dto) {
    return ResponseEntity.ok(memberService.update(principalDetails.getMember() ,dto));
  }

  @DeleteMapping("/withdrawal")
  public ResponseEntity<?> withdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestBody MemberWithdrawalDto dto) {
    return ResponseEntity.ok(memberService.withdrawal(principalDetails.getMember(), dto));
  }
}
