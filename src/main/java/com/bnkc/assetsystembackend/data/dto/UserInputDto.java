package com.bnkc.assetsystembackend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Schema(description = "User Input")
public record UserInputDto(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Join Date in required") LocalDate joinDate,
        LocalDate endDate,
        @NotNull(message = "Branch Id is required") Long branchId,
        @NotNull(message = "Department Id is required") Long departmentId,
        @NotNull(message = "Job Position Id is required") Long jobPositionId,
        @NotEmpty(message = "Role Ids it required") Set<Long> roleIds
) {
}
