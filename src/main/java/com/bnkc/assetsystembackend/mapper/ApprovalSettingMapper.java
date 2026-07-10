package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.ApprovalLineDto;
import com.bnkc.assetsystembackend.data.dto.ApprovalSettingDto;
import com.bnkc.assetsystembackend.entity.ApprovalLine;
import com.bnkc.assetsystembackend.entity.ApprovalSetting;
import com.bnkc.assetsystembackend.entity.Branch;
import com.bnkc.assetsystembackend.entity.Department;
import com.bnkc.assetsystembackend.entity.JobPosition;
import com.bnkc.assetsystembackend.entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ApprovalSettingMapper {

    @Mapping(source = "approvalLines", target = "approvalLines", qualifiedByName = "toOrderedLineDtos")
    ApprovalSettingDto toDto(ApprovalSetting entity);

    List<ApprovalSettingDto> toDto(List<ApprovalSetting> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approvalLines", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ApprovalSetting toEntity(ApprovalSettingDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approvalLines", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ApprovalSetting mergeDto(ApprovalSettingDto dto, @MappingTarget ApprovalSetting entity);

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "branch.shortName", target = "branchShortName")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "department.shortName", target = "departmentShortName")
    @Mapping(source = "jobPosition.id", target = "jobPositionId")
    @Mapping(source = "jobPosition.name", target = "jobPositionName")
    @Mapping(source = "jobPosition.shortName", target = "jobPositionShortName")
    ApprovalLineDto toDto(ApprovalLine entity);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.label", target = "label")
    @Mapping(source = "dto.action", target = "action")
    @Mapping(source = "lineOrder", target = "lineOrder")
    @Mapping(source = "approvalSetting", target = "approvalSetting")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "branch", target = "branch")
    @Mapping(source = "dto.useRequestorBranch", target = "useRequestorBranch")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "dto.useRequestorDepartment", target = "useRequestorDepartment")
    @Mapping(source = "jobPosition", target = "jobPosition")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ApprovalLine toEntity(ApprovalLineDto dto,
                          Integer lineOrder,
                          ApprovalSetting approvalSetting,
                          Role role,
                          Branch branch,
                          Department department,
                          JobPosition jobPosition);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.label", target = "label")
    @Mapping(source = "dto.action", target = "action")
    @Mapping(source = "lineOrder", target = "lineOrder")
    @Mapping(source = "approvalSetting", target = "approvalSetting")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "branch", target = "branch")
    @Mapping(source = "dto.useRequestorBranch", target = "useRequestorBranch")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "dto.useRequestorDepartment", target = "useRequestorDepartment")
    @Mapping(source = "jobPosition", target = "jobPosition")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ApprovalLine mergeDto(ApprovalLineDto dto,
                          Integer lineOrder,
                          ApprovalSetting approvalSetting,
                          Role role,
                          Branch branch,
                          Department department,
                          JobPosition jobPosition,
                          @MappingTarget ApprovalLine entity);

    @Named("toOrderedLineDtos")
    default List<ApprovalLineDto> toOrderedLineDtos(List<ApprovalLine> approvalLines) {
        if (approvalLines == null) {
            return List.of();
        }

        return approvalLines.stream()
                .sorted(Comparator.comparing(ApprovalLine::getLineOrder))
                .map(this::toDto)
                .toList();
    }
}

