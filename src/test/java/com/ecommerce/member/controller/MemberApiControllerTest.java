package com.ecommerce.member.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.annotation.CustomMockMember;
import com.ecommerce.config.SecurityConfig;
import com.ecommerce.global.security.auth.PrincipalOauth2UserService;
import com.ecommerce.global.security.jwt.JwtRedisService;
import com.ecommerce.global.security.jwt.Oauth2FailureHandler;
import com.ecommerce.global.security.jwt.Oauth2SuccessHandler;
import com.ecommerce.global.security.jwt.TokenProvider;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignInResponseDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({MemberApiController.class, SecurityConfig.class})
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    JwtRedisService jwtRedisService;

    @MockBean
    PrincipalOauth2UserService principalOauth2UserService;

    @MockBean
    Oauth2SuccessHandler oauth2SuccessHandler;

    @MockBean
    Oauth2FailureHandler oauth2FailureHandler;

    @MockBean
    MemberService memberService;

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() throws Exception {
        MemberSignupDto dto = MemberSignupDto.builder()
            .memberId("qwer12")
            .email("qwer12@email.com")
            .password("qwer12")
            .phone("01011112222")
            .authNum("1234")
            .build();

        mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login() throws Exception {
        MemberSignInRequestDto request = MemberSignInRequestDto.builder()
            .memberId("qwer12")
            .password("qwer12")
            .build();

        MemberSignInResponseDto response = MemberSignInResponseDto.builder()
            .memberId("qwer12")
            .accessToken("accessToken")
            .build();

        when(memberService.signIn(argThat(arg -> arg.getMemberId().equals("qwer12")))).thenReturn(response);

        mockMvc.perform(post("/api/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberId").value("qwer12"))
            .andExpect(jsonPath("$.accessToken").value("accessToken"));
    }

    @Test
    @DisplayName("소셜 로그인 전화번호 등록 - 성공")
    @CustomMockMember
    void updateOauth2() throws Exception {

        MemberOAuthUpdateDto dto = MemberOAuthUpdateDto.builder()
            .phone("01011112222")
            .build();

        mockMvc.perform(patch("/api/member/update/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원정보 업데이트 - 성공")
    @CustomMockMember
    void update() throws Exception {

        MemberUpdateDto dto = MemberUpdateDto.builder()
            .phone("01011112222")
            .password("qwer12")
            .build();

        mockMvc.perform(patch("/api/member/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    @CustomMockMember
    void logout() throws Exception {

        mockMvc.perform(post("/api/member/logout")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴 - 성공")
    @CustomMockMember
    void withdrawal() throws Exception {

        MemberWithdrawalDto dto = MemberWithdrawalDto.builder()
            .password("qwer12")
            .build();

        mockMvc.perform(post("/api/member/withdrawal")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isOk());
    }
}