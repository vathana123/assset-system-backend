package com.bnkc.assetsystembackend.controller;

import com.bnkc.assetsystembackend.data.dto.JobPositionDto;
import com.bnkc.assetsystembackend.service.JobPositionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("job-positions")
public class JobPositionController {
    private final JobPositionService service;

//    @PreAuthorize("hasAuthority('JOB_POSITION_READ')")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Map<String, Object> filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(filters, pageable));
    }

//    @PreAuthorize("hasAuthority('JOB_POSITION_READ')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

//    @PreAuthorize("hasAuthority('JOB_POSITION_CREATE')")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> save(@RequestBody @Valid JobPositionDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

//    @PreAuthorize("hasAuthority('JOB_POSITION_UPDATE')")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid JobPositionDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

//    @PreAuthorize("hasAuthority('JOB_POSITION_DELETE')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of(
                "message", "%s %s has been deleted.".formatted(this.getClass().getSimpleName().replace("Controller", ""), id)));
    }
}
