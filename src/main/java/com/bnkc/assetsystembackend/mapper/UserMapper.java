package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.UserDto;
import com.bnkc.assetsystembackend.data.dto.UserInputDto;
import com.bnkc.assetsystembackend.data.dto.UserRoleDto;
import com.bnkc.assetsystembackend.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "jobPosition.id", target = "jobPositionId")
    @Mapping(source = "jobPosition.code", target = "jobPositionCode")
    @Mapping(source = "jobPosition.name", target = "jobPositionName")
    @Mapping(source = "jobPosition.shortName", target = "jobPositionShortName")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.code", target = "branchCode")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "branch.shortName", target = "branchShortName")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.code", target = "departmentCode")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "department.shortName", target = "departmentShortName")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "toUserRoles")
    @Mapping(source = "roles", target = "permissions", qualifiedByName = "toPermissionNames")
    UserDto toDto(UserInfo entity);
    List<UserDto> toDto(List<UserInfo> entities);

    @Mapping(source = "dto.username", target = "username")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "roles", target = "roles")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "changedPasswordAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    UserInfo toEntity(UserInputDto dto,
                      JobPosition jobPosition,
                      Branch branch,
                      Department department,
                      Set<Role> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.username", target = "username")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "roles", target = "roles")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "changedPasswordAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    UserInfo mergeDto(UserInputDto dto,
                      JobPosition jobPosition,
                      Branch branch,
                      Department department,
                      Set<Role> roles,
                      @MappingTarget UserInfo entity);

    @Named("toUserRoles")
    default Set<UserRoleDto> toUserRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(role ->
                    UserRoleDto.builder().id(role.getId()).name(role.getName()).build()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Named("toPermissionNames")
    default Set<String> toPermissionNames(Set<Role> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .flatMap(role -> role.getPermissions() == null ? java.util.stream.Stream.empty() : role.getPermissions().stream())
                .filter(Objects::nonNull)
                .map(Permission::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
