package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.PermissionDto;
import com.bnkc.assetsystembackend.data.dto.RoleDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.Permission;
import com.bnkc.assetsystembackend.entity.Role;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.PermissionMapper;
import com.bnkc.assetsystembackend.mapper.RoleMapper;
import com.bnkc.assetsystembackend.repository.PermissionRepository;
import com.bnkc.assetsystembackend.repository.RoleRepository;
import com.bnkc.assetsystembackend.service.RoleService;
import com.bnkc.assetsystembackend.specification.Search;
import com.bnkc.assetsystembackend.util.PageResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper mapper;
    private final PermissionMapper permissionMapper;

    Role getEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Permission.class, id));
    }

    public Set<Permission> getPermissionByIds(Set<Long> ids) {

        List<Permission> permissions = permissionRepository.findAllById(ids);

        Set<Long> foundIds = permissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        for (Long id : ids) {
            if (!foundIds.contains(id)) {
                throw new ResourceNotFoundException(Permission.class, id);
            }
        }

        return new HashSet<>(permissions);
    }

    @Override
    public PageResponse<RoleDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<Role>builder()
                                .filters(filters)
                                .fields(List.of("name"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        return permissionMapper.toDto(permissionRepository.findAll());
    }

    @Override
    public RoleDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public RoleDto save(RoleDto dto) {
        if(repository.existsByName(dto.name())){
            throw new ValidationException("Name %s already exist.".formatted(dto.name()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto, getPermissionByIds(dto.permissionIds()))));
    }

    @Override
    public RoleDto update(Long id, RoleDto dto) {
        if(repository.existsByNameAndIdNot(dto.name(), id)){
            throw new ValidationException("Name %s already exist.".formatted(dto.name()));
        }
        return mapper.toDto(repository.save(mapper.mergeDto(dto, getPermissionByIds(dto.permissionIds()), getEntityById(id))));
    }

    @Override
    public void delete(Long aLong) {
        repository.delete(getEntityById(aLong));
    }
}
