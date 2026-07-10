package com.bnkc.assetsystembackend.controller;

import com.bnkc.assetsystembackend.data.dto.RoleDto;
import com.bnkc.assetsystembackend.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("roles")
public class RoleController {
    private final RoleService service;

//    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Map<String, Object> filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(filters, pageable));
    }

    //    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/permissions")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAllPermissions(
    ) {
        return ResponseEntity.ok(service.getAllPermissions());
    }

    //    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    //    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> save(@RequestBody @Valid RoleDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

//    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid RoleDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

//    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of(
                "message", "%s %s has been deleted.".formatted(this.getClass().getSimpleName().replace("Controller", ""), id)));
    }
}
