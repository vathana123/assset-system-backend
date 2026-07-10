package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Schema(description = "User")
public record UserDto(
        Long id,
        String username,
        String name,
        LocalDate joinDate,
        LocalDate endDate,
        Long branchId,
        String branchCode,
        String branchName,
        String branchShortName,
        Long departmentId,
        String departmentCode,
        String departmentName,
        String departmentShortName,
        Long jobPositionId,
        String jobPositionCode,
        String jobPositionName,
        String jobPositionShortName,
        Set<UserRoleDto> roles,
        Set<String> permissions
) {
}
