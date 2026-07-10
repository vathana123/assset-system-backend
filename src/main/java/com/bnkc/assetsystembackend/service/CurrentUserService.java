package com.bnkc.assetsystembackend.service;

import com.bnkc.assetsystembackend.entity.UserInfo;

import java.util.Set;

public interface CurrentUserService {
    UserInfo getCurrentUser();

    String getUsername();

    Set<String> getPermissions();
}
