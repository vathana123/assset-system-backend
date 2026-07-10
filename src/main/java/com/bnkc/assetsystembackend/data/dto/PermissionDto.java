package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Permission")
public record PermissionDto(
        Long id,
        String name,
        String resourceTarget
) {
}
