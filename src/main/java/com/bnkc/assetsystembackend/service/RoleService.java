package com.bnkc.assetsystembackend.service;

import com.bnkc.assetsystembackend.data.dto.PermissionDto;
import com.bnkc.assetsystembackend.data.dto.RoleDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RoleService {
    PageResponse<RoleDto> findAll(Map<String, Object> filters, Pageable pageable);
    List<PermissionDto> getAllPermissions();
    RoleDto findById(Long id);
    RoleDto save(RoleDto dto);
    RoleDto update(Long id, RoleDto dto);
    void delete(Long id);
}
