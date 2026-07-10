package com.bnkc.assetsystembackend.config.security;

import com.bnkc.assetsystembackend.entity.Permission;
import com.bnkc.assetsystembackend.entity.Role;
import com.bnkc.assetsystembackend.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserAuthorityMapper {
    private UserAuthorityMapper() {
    }

    public static Set<String> roleNames(UserInfo user) {
        if (user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(Role::getName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Set<String> permissionNames(UserInfo user) {
        if (user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .flatMap(role -> role.getPermissions() == null ? Stream.empty() : role.getPermissions().stream())
                .filter(Objects::nonNull)
                .map(UserAuthorityMapper::permissionName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Collection<? extends GrantedAuthority> authorities(UserInfo user) {
        Set<String> authorities = new TreeSet<>();

        roleNames(user).stream()
                .map(UserAuthorityMapper::roleAuthority)
                .forEach(authorities::add);

        authorities.addAll(permissionNames(user));

        authorities.addAll(rawPermissionNames(user));

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private static Set<String> rawPermissionNames(UserInfo user) {
        if (user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .flatMap(role -> role.getPermissions() == null ? Stream.empty() : role.getPermissions().stream())
                .filter(Objects::nonNull)
                .map(Permission::getName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static String permissionName(Permission permission) {
        if (!StringUtils.hasText(permission.getName())) {
            return null;
        }

        if (!StringUtils.hasText(permission.getResourceTarget())) {
            return permission.getName();
        }

        return permission.getName() + ":" + permission.getResourceTarget();
    }

    private static String roleAuthority(String roleName) {
        return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
    }
}
