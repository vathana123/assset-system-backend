package com.bnkc.assetsystembackend.config.security;

import com.bnkc.assetsystembackend.entity.UserInfo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
public class AuthenticatedUser implements UserDetails {
    private final UserInfo userInfo;
    private final Set<String> roles;
    private final Set<String> permissions;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.roles = UserAuthorityMapper.roleNames(userInfo);
        this.permissions = UserAuthorityMapper.permissionNames(userInfo);
        this.authorities = UserAuthorityMapper.authorities(userInfo);
    }

    public Long getId() {
        return userInfo.getId();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
