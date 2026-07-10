package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.ApprovalLineDto;
import com.bnkc.assetsystembackend.data.dto.ApprovalSettingDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.ApprovalLine;
import com.bnkc.assetsystembackend.entity.ApprovalSetting;
import com.bnkc.assetsystembackend.entity.Branch;
import com.bnkc.assetsystembackend.entity.Department;
import com.bnkc.assetsystembackend.entity.JobPosition;
import com.bnkc.assetsystembackend.entity.Role;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.ApprovalSettingMapper;
import com.bnkc.assetsystembackend.repository.ApprovalSettingRepository;
import com.bnkc.assetsystembackend.repository.BranchRepository;
import com.bnkc.assetsystembackend.repository.DepartmentRepository;
import com.bnkc.assetsystembackend.repository.JobPositionRepository;
import com.bnkc.assetsystembackend.repository.RoleRepository;
import com.bnkc.assetsystembackend.service.ApprovalSettingService;
import com.bnkc.assetsystembackend.specification.Search;
import com.bnkc.assetsystembackend.util.Entity;
import com.bnkc.assetsystembackend.util.PageResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalSettingServiceImpl implements ApprovalSettingService {

    private final ApprovalSettingRepository repository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final JobPositionRepository jobPositionRepository;
    private final ApprovalSettingMapper mapper;

    @Override
    public PageResponse<ApprovalSettingDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper.toPageResponse(
                repository.findAll(Search
                        .<ApprovalSetting>builder()
                        .filters(filters)
                        .fields(List.of("name"))
                        .build(), pageable),
                mapper::toDto);
    }

    @Override
    public ApprovalSettingDto findById(Long id) {
        return mapper.toDto(getEntityById(id));
    }

    @Override
    public ApprovalSettingDto save(ApprovalSettingDto dto) {
        ApprovalSetting approvalSetting = mapper.toEntity(dto);
        mergeApprovalLines(approvalSetting, dto.approvalLines());
        return mapper.toDto(repository.save(approvalSetting));
    }

    @Override
    public ApprovalSettingDto update(Long id, ApprovalSettingDto dto) {
        ApprovalSetting approvalSetting = mapper.mergeDto(dto, getEntityById(id));
        mergeApprovalLines(approvalSetting, dto.approvalLines());
        return mapper.toDto(repository.save(approvalSetting));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getEntityById(id));
    }

    private ApprovalSetting getEntityById(Long id) {
        return Entity.getById(repository, id, ApprovalSetting.class);
    }

    private void mergeApprovalLines(ApprovalSetting approvalSetting, List<ApprovalLineDto> lineDtos) {
        Map<Long, ApprovalLine> existingLines = new HashMap<>();
        approvalSetting.getApprovalLines().stream()
                .filter(line -> line.getId() != null)
                .forEach(line -> existingLines.put(line.getId(), line));

        Set<Long> requestedIds = new HashSet<>();

        for (int index = 0; index < lineDtos.size(); index++) {
            ApprovalLineDto lineDto = lineDtos.get(index);
            ApprovalLine approvalLine;

            if (lineDto.id() == null) {
                approvalLine = createApprovalLine(approvalSetting, lineDto, index + 1);
                approvalSetting.addApprovalLine(approvalLine);
                continue;
            }

            if (!requestedIds.add(lineDto.id())) {
                throw new ValidationException("Approval Line ID %s is duplicated.".formatted(lineDto.id()));
            }

            approvalLine = existingLines.remove(lineDto.id());
            if (approvalLine == null) {
                throw new ResourceNotFoundException(ApprovalLine.class, lineDto.id());
            }

            mergeApprovalLine(approvalSetting, approvalLine, lineDto, index + 1);
        }

        approvalSetting.getApprovalLines().removeAll(existingLines.values());
        approvalSetting.getApprovalLines().sort(Comparator.comparing(ApprovalLine::getLineOrder));
    }

    private ApprovalLine createApprovalLine(ApprovalSetting approvalSetting,
                                            ApprovalLineDto dto,
                                            Integer lineOrder) {
        return mapper.toEntity(
                dto,
                lineOrder,
                approvalSetting,
                getRoleById(dto.roleId()),
                getBranchById(dto.branchId()),
                getDepartmentById(dto.departmentId()),
                getJobPositionById(dto.jobPositionId()));
    }

    private void mergeApprovalLine(ApprovalSetting approvalSetting,
                                   ApprovalLine approvalLine,
                                   ApprovalLineDto dto,
                                   Integer lineOrder) {
        mapper.mergeDto(
                dto,
                lineOrder,
                approvalSetting,
                getRoleById(dto.roleId()),
                getBranchById(dto.branchId()),
                getDepartmentById(dto.departmentId()),
                getJobPositionById(dto.jobPositionId()),
                approvalLine);
    }

    private Role getRoleById(Long id) {
        return id == null ? null : Entity.getById(roleRepository, id, Role.class);
    }

    private Branch getBranchById(Long id) {
        return id == null ? null : Entity.getById(branchRepository, id, Branch.class);
    }

    private Department getDepartmentById(Long id) {
        return id == null ? null : Entity.getById(departmentRepository, id, Department.class);
    }

    private JobPosition getJobPositionById(Long id) {
        return id == null ? null : Entity.getById(jobPositionRepository, id, JobPosition.class);
    }
}
