package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Job Position")
public record JobPositionDto(
        Long id,
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Short Name is required")
        String shortName,
        String localName,
        @NotNull(message = "Job Level Id is required")
        Long jobLevelId,
        String jobLevelCode,
        String jobLevelName
) {
}
