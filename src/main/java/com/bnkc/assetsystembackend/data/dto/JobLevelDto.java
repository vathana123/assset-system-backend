package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Job Level")
public record JobLevelDto(
        Long id,
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Short Name is required")
        String shortName,
        String localName
) {
}
