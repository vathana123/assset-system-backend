package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Approval Setting")
public record ApprovalSettingDto(
        Long id,
        @NotBlank(message = "Name is required")
        String name,
        @NotEmpty(message = "Approval Lines are required")
        List<@Valid ApprovalLineDto> approvalLines
) {
}
