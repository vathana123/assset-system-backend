package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.PermissionDto;
import com.bnkc.assetsystembackend.entity.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionDto toDto(Permission entity);
    List<PermissionDto> toDto(List<Permission> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permission toEntity(PermissionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permission mergeDto(PermissionDto dto, @MappingTarget Permission entity);
}
