package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.exception.UnauthorizedException;
import com.bnkc.assetsystembackend.repository.UserRepository;
import com.bnkc.assetsystembackend.config.security.AuthenticatedUser;
import com.bnkc.assetsystembackend.config.security.UserAuthorityMapper;
import com.bnkc.assetsystembackend.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserInfo getCurrentUser() {
        return userRepository.findByUsernameWithRolesAndPermissions(getUsername())
                .orElseThrow(() -> new UnauthorizedException("Authenticated user was not found."));
    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("Authentication is required.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser.getUsername();
        }

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        if (principal instanceof String username && StringUtils.hasText(username) && !"anonymousUser".equals(username)) {
            return username;
        }

        throw new UnauthorizedException("Authentication is required.");
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser.getPermissions();
        }

        return UserAuthorityMapper.permissionNames(getCurrentUser());
    }
}
