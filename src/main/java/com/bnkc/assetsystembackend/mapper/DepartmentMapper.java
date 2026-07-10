package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.DepartmentDto;
import com.bnkc.assetsystembackend.entity.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto toDto(Department entity);
    List<DepartmentDto> toDto(List<Department> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Department toEntity(DepartmentDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Department mergeDto(DepartmentDto dto, @MappingTarget Department entity);
}
