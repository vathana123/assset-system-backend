package com.bnkc.assetsystembackend.controller;

import com.bnkc.assetsystembackend.data.dto.ApprovalSettingDto;
import com.bnkc.assetsystembackend.service.ApprovalSettingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("approval-settings")
public class ApprovalSettingController {
    private final ApprovalSettingService service;

//    @PreAuthorize("hasAuthority('APPROVAL_SETTING_READ')")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Map<String, Object> filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(filters, pageable));
    }

//    @PreAuthorize("hasAuthority('APPROVAL_SETTING_READ')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

//    @PreAuthorize("hasAuthority('APPROVAL_SETTING_CREATE')")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> save(@RequestBody @Valid ApprovalSettingDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

//    @PreAuthorize("hasAuthority('APPROVAL_SETTING_UPDATE')")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ApprovalSettingDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

//    @PreAuthorize("hasAuthority('APPROVAL_SETTING_DELETE')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of(
                "message", "%s %s has been deleted.".formatted(this.getClass().getSimpleName().replace("Controller", ""), id)));
    }
}
