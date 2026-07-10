package com.bnkc.assetsystembackend.data.dto;

import com.bnkc.assetsystembackend.entity.AssetOwnerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Asset Owner")
public record AssetOwnerDto(
        Long id,
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Asset Owner Type is required")
        AssetOwnerType type,
        @NotNull(message = "Branch Id is required")
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
        String jobPositionShortName
) {
}
