package com.remu.global.config.sercurity.oauth;

import com.remu.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private final User user;
    private Long id;    // DB에 저장된 실제 PK
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // 소셜에서 받은 원본 데이터

    private final boolean isNewUser; // 새로운 유저, 기존 유저 구분

    public UserPrincipal(User user, Map<String, Object> attributes, boolean isNewUser) {
        this.user = user;
        this.id = user.getId();
        this.email = user.getEmail();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRoleKey()));
        this.attributes = attributes;
        this.isNewUser = isNewUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인은 비밀번호가 없으므로 null 반환
    }

    @Override
    public String getUsername() {
        return email; // 시큐리티에서 유저를 식별할 ID로 이메일 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료 안됨)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부 (true: 안잠김)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부 (true: 만료 안됨)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성)
    }
}
