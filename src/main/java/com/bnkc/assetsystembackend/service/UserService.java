package com.bnkc.assetsystembackend.service;

import com.bnkc.assetsystembackend.data.dto.UserDto;
import com.bnkc.assetsystembackend.data.dto.UserInputDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    PageResponse<UserDto> findAll(Map<String, Object> filters, Pageable pageable);
    UserDto findById(Long id);
    UserDto save(UserInputDto dto);
    UserDto update(Long id, UserInputDto dto);
    void delete(Long id);
}
