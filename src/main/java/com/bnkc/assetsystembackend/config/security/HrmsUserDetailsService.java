package com.bnkc.assetsystembackend.config.security;

import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HrmsUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return new AuthenticatedUser(loadDomainUserByUsername(username));
    }

    @Transactional(readOnly = true)
    public UserInfo loadDomainUserByUsername(String username) {
        return userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found.".formatted(username)));
    }
}
