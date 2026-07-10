package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Set;

@Builder
@Schema(description = "Role")
public record RoleDto(
        Long id,
        @NotBlank(message = "Name is required")
        String name,
        @NotEmpty(message = "Permission Ids are required")
        Set<Long> permissionIds
) {
}
