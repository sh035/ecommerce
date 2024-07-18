package com.ecommerce.config;

import com.ecommerce.global.security.jwt.JwtAuthenticationFilter;
import com.ecommerce.global.security.jwt.Oauth2SuccessHandler;
import com.ecommerce.global.security.auth.PrincipalOauth2UserService;
import com.ecommerce.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenProvider tokenProvider;
  private final PrincipalOauth2UserService principalOauth2UserService;
  private final Oauth2SuccessHandler oauth2SuccessHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement
            -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> request
//            .requestMatchers("/api/member/signup", "/api/member/login").anonymous()
//            .requestMatchers("api/member/**").hasRole("USER")
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/**").permitAll()
            .anyRequest().authenticated()
        )
        // oauth2 설정
        .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
            // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
            oauth.userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
                // 로그인 성공 시 핸들러
                .successHandler(oauth2SuccessHandler)
        )
        .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
