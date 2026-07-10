package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.UserDto;
import com.bnkc.assetsystembackend.data.dto.UserInputDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.*;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.UserMapper;
import com.bnkc.assetsystembackend.repository.*;
import com.bnkc.assetsystembackend.service.UserService;
import com.bnkc.assetsystembackend.specification.Search;
import com.bnkc.assetsystembackend.util.Entity;
import com.bnkc.assetsystembackend.util.PageResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final JobPositionRepository jobPositionRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public Set<Role> getRoleByIds(Set<Long> ids) {

        List<Role> roles = roleRepository.findAllById(ids);

        Set<Long> foundIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        for (Long id : ids) {
            if (!foundIds.contains(id)) {
                throw new ResourceNotFoundException(Role.class, id);
            }
        }

        return new HashSet<>(roles);
    }

    @Override
    public PageResponse<UserDto> findAll(Map<String, Object> filters, Pageable pageable) {
        Page<UserInfo> userPage = repository.findAll(Search
                .<UserInfo>builder()
                .filters(filters)
                .fields(List.of("username", "name"))
                .build(), pageable);
        List<UserDto> userDtoList = mapper.toDto(userPage.getContent());
        userDtoList.forEach(userDto -> userDto.permissions().clear());
        return PageResponseMapper
                .toPageResponse(
                        userPage,
                        userDtoList);
    }

    @Override
    public UserDto findById(Long id) {
        return mapper.toDto(Entity.getById(repository, id, UserInfo.class));
    }

    @Override
    public UserDto save(UserInputDto dto) {
        if(repository.existsByUsername(dto.username())){
            throw new ValidationException("Name %s already exist.".formatted(dto.username()));
        }

        UserInfo user = mapper.toEntity(
                dto,
                Entity.getById(jobPositionRepository, dto.jobPositionId(), JobPosition.class),
                Entity.getById(branchRepository, dto.branchId(), Branch.class),
                Entity.getById(departmentRepository, dto.departmentId(), Department.class),
                getRoleByIds(dto.roleIds())
        );
        user.setPassword(passwordEncoder.encode(dto.username()));
        user.setChangedPasswordAt(LocalDateTime.now());

        return mapper.toDto(repository.save(user));
    }

    @Override
    public UserDto update(Long id, UserInputDto dto) {
        if(repository.existsByUsernameAndIdNot(dto.username(), id)){
            throw new ValidationException("Name %s already exist.".formatted(dto.username()));
        }

        UserInfo user = mapper.mergeDto(
                dto,
                Entity.getById(jobPositionRepository, dto.jobPositionId(), JobPosition.class),
                Entity.getById(branchRepository, dto.branchId(), Branch.class),
                Entity.getById(departmentRepository, dto.departmentId(), Department.class),
                getRoleByIds(dto.roleIds()),
                Entity.getById(repository, id, UserInfo.class)
        );

        return mapper.toDto(repository.save(user));
    }

    @Override
    public void delete(Long id) {
        repository.delete(Entity.getById(repository, id, UserInfo.class));
    }
}
