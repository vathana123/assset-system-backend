package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "User Role")
public record UserRoleDto(
        Long id,
        String name
) {
}
