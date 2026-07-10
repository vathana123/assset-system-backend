package com.bnkc.assetsystembackend.controller;

import com.bnkc.assetsystembackend.data.dto.BranchDto;
import com.bnkc.assetsystembackend.service.BranchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("branches")
public class BranchController {
    private final BranchService service;

//    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Map<String, Object> filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(filters, pageable));
    }

//    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

//    @PreAuthorize("hasAuthority('BRANCH_CREATE')")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> save(@RequestBody @Valid BranchDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

//    @PreAuthorize("hasAuthority('BRANCH_UPDATE')")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid BranchDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

//    @PreAuthorize("hasAuthority('BRANCH_DELETE')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of(
                "message", "%s %s has been deleted.".formatted(this.getClass().getSimpleName().replace("Controller", ""), id)));
    }
}
