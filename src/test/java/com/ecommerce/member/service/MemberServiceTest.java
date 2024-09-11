package com.ecommerce.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.global.security.jwt.JwtRedisService;
import com.ecommerce.global.security.jwt.JwtTokenDto;
import com.ecommerce.global.security.jwt.TokenProvider;
import com.ecommerce.mail.service.MailRedisService;
import com.ecommerce.mail.service.MailService;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignInResponseDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    MailRedisService mailRedisService;

    @Mock
    MailService mailService;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    JwtRedisService jwtRedisService;

    @Mock
    MockHttpServletRequest request;

    @Mock
    MockHttpServletResponse response;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() throws Exception {

        MemberSignupDto dto = MemberSignupDto.builder()
            .memberId("qwer12")
            .password("qwer12")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .authNum("1234")
            .build();

        Member member = Member.builder()
            .memberId("qwer12")
            .password("qwer12")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .role(Role.USER)
            .build();

        when(memberRepository.existsByMemberId(dto.getMemberId())).thenReturn(false);
        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(dto.getPassword());
        when(mailRedisService.getData("1234")).thenReturn("qwer12@gmail.com");
        when(memberRepository.save(argThat(arg -> arg.getMemberId().equals("qwer12"))))
            .thenReturn(member);

        memberService.signUp(dto);

        verify(memberRepository, times(1)).existsByMemberId(dto.getMemberId());
        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(mailRedisService, times(1)).getData("1234");

        verify(memberRepository, times(1)).save(
            argThat(arg -> arg.getMemberId().equals("qwer12")));
        verify(memberRepository, times(1)).save(
            argThat(arg -> arg.getEmail().equals("qwer12@gmail.com")));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void signIn() throws Exception {
        MemberSignInRequestDto dto = MemberSignInRequestDto.builder()
            .memberId("qwer12")
            .password("qwer12")
            .build();

        Member member = Member.builder()
            .memberId("qwer12")
            .password("encodedPassword")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .role(Role.USER)
            .build();

        JwtTokenDto tokenDto = JwtTokenDto.builder()
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .build();

        long refreshTokenExpiration = 20000L;

        when(memberRepository.findByMemberId(dto.getMemberId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(dto.getPassword(), member.getPassword())).thenReturn(true);
        when(tokenProvider.generateToken(member.getMemberId(), Collections.singletonList(
            new SimpleGrantedAuthority(Role.USER.toString())))).thenReturn(tokenDto);
        when(tokenProvider.getExpiration(tokenDto.getRefreshToken())).thenReturn(refreshTokenExpiration);

        MemberSignInResponseDto responseDto = memberService.signIn(dto);

        verify(memberRepository, times(1)).findByMemberId(dto.getMemberId());
        verify(passwordEncoder, times(1)).matches(dto.getPassword(), member.getPassword());
        verify(tokenProvider, times(1)).generateToken(member.getMemberId(),
            Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString())));
        verify(jwtRedisService, times(1)).save(tokenDto, refreshTokenExpiration);

        assertEquals(member.getMemberId(), responseDto.getMemberId());
        assertEquals(tokenDto.getAccessToken(), responseDto.getAccessToken());
    }

    @Test
    @DisplayName("소셜 로그인 전화번호 추가 - 성공")
    void updatePhone() throws Exception {
        MemberOAuthUpdateDto dto = MemberOAuthUpdateDto.builder()
            .phone("01011112222")
            .build();

        String memberId = "qwer12";

        Member member = Member.builder()
            .memberId("qwer12")
            .password("encodedPassword")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .role(Role.USER)
            .build();

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
        member.updatePhone(dto.getPhone());

        memberService.updatePhone(memberId, dto);

        verify(memberRepository, times(1)).save(member);
    }

    @Test
    @DisplayName("회원 정보 업데이트(패스워드, 전화번호) - 성공")
    void update() throws Exception {
        MemberUpdateDto dto = MemberUpdateDto.builder()
            .password("qweqew21")
            .phone("01022223333")
            .build();

        String memberId = "qwer12";

        Member member = Member.builder()
            .memberId(memberId)
            .password("encodedPassword")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .role(Role.USER)
            .build();

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(dto.getPassword(), "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedNewPassword");

        memberService.update(memberId, dto);

        verify(memberRepository, times(1)).save(member);
        assertEquals("encodedNewPassword", member.getPassword());
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void logout() throws Exception {
        String memberId = "qwer12";
        String accessToken = "accessToken";
        long accessTokenExpiration = 20000L;

        when(tokenProvider.resolveToken(request)).thenReturn(accessToken);
        when(tokenProvider.getExpiration(accessToken)).thenReturn(accessTokenExpiration);

        memberService.logout(memberId, request);

        verify(tokenProvider, times(1)).resolveToken(request);
        verify(tokenProvider, times(1)).getExpiration(accessToken);
        verify(jwtRedisService, times(1)).deleteRefreshTokenByAccessToken(accessToken);
        verify(jwtRedisService, times(1)).setBlackList(accessToken, memberId,
            accessTokenExpiration);
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공")
    void withdrawal() throws Exception {
        String memberId = "qwer12";
        MemberWithdrawalDto dto = MemberWithdrawalDto.builder()
            .password("encodedPassword")
            .build();

        Member member = Member.builder()
            .memberId(memberId)
            .password("encodedPassword")
            .email("qwer12@gmail.com")
            .phone("01011112222")
            .role(Role.USER)
            .build();

        LocalDateTime now = LocalDateTime.now();

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(dto.getPassword(), "encodedPassword")).thenReturn(true);

        memberService.withdrawal(memberId, dto);

        verify(memberRepository, times(1)).save(member);
        assertEquals(member.getDeletedAt().getMinute(), now.getMinute());
    }
}