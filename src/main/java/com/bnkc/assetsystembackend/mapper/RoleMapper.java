package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.RoleDto;
import com.bnkc.assetsystembackend.entity.Permission;
import com.bnkc.assetsystembackend.entity.Role;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissionIds", source = "permissions")
    RoleDto toDto(Role entity);

    List<RoleDto> toDto(List<Role> entities);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "permissions", target = "permissions")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Role toEntity(RoleDto dto, Set<Permission> permissions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "permissions", target = "permissions")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Role mergeDto(RoleDto dto, Set<Permission> permissions, @MappingTarget Role entity);

    default Set<Long> map(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }

        return permissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
    }
}
