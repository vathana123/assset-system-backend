package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Approval")
public record ApprovalLineDto(
        Long id,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Label is required")
        String label,
        @NotBlank(message = "Action is required")
        String action,
        Integer lineOrder,
        Long roleId,
        String roleName,
        Long branchId,
        String branchName,
        String branchShortName,
        boolean useRequestorBranch,
        Long departmentId,
        String departmentName,
        String departmentShortName,
        boolean useRequestorDepartment,
        Long jobPositionId,
        String jobPositionName,
        String jobPositionShortName
) {
}
