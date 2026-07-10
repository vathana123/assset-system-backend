package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.AssetOwnerDto;
import com.bnkc.assetsystembackend.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetOwnerMapper {
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
    AssetOwnerDto toDto(AssetOwner entity);
    List<AssetOwnerDto> toDto(List<AssetOwner> entities);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.code", target = "code")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AssetOwner toEntity(AssetOwnerDto dto,
                      JobPosition jobPosition,
                      Branch branch,
                      Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.code", target = "code")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AssetOwner mergeDto(AssetOwnerDto dto,
                      JobPosition jobPosition,
                      Branch branch,
                      Department department,
                      @MappingTarget AssetOwner entity);
}
