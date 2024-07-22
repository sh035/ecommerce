package com.ecommerce.global.security.auth;

import com.ecommerce.member.domain.entity.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Builder
public class PrincipalDetails implements OAuth2User, UserDetails {
  private Long id;
  private String email;
  private String password;
  private String role;
  private String nickname;
  private Map<String, Object> attributes;

  // 일반 로그인
  public PrincipalDetails(Long id, String email, String password, String role, String nickname) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.nickname = nickname;
  }

  // OAuth2 로그인
  public PrincipalDetails(Long id, String email, String password, String role, String nickname, Map<String, Object> attributes) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.nickname = nickname;
    this.attributes = attributes;
  }


  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add((GrantedAuthority) () -> role);
    return collection;
  }

  @Override
  public String getName() {
    return nickname;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }
}
