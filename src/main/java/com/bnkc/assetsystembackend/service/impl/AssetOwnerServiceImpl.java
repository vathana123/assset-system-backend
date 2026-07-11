package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.AssetOwnerDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.AssetOwner;
import com.bnkc.assetsystembackend.entity.Branch;
import com.bnkc.assetsystembackend.entity.Department;
import com.bnkc.assetsystembackend.entity.JobPosition;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.AssetOwnerMapper;
import com.bnkc.assetsystembackend.repository.AssetOwnerRepository;
import com.bnkc.assetsystembackend.repository.BranchRepository;
import com.bnkc.assetsystembackend.repository.DepartmentRepository;
import com.bnkc.assetsystembackend.repository.JobPositionRepository;
import com.bnkc.assetsystembackend.service.AssetOwnerService;
import com.bnkc.assetsystembackend.specification.Search;
import com.bnkc.assetsystembackend.util.Entity;
import com.bnkc.assetsystembackend.util.PageResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetOwnerServiceImpl implements AssetOwnerService {

    private final AssetOwnerRepository repository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final JobPositionRepository jobPositionRepository;
    private final AssetOwnerMapper mapper;

    AssetOwner getEntityById(Long id) {
        return Entity.getById(repository, id, AssetOwner.class);
    }

    @Override
    public PageResponse<AssetOwnerDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<AssetOwner>builder()
                                .filters(filters)
                                .fields(List.of("code", "name"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public AssetOwnerDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public AssetOwnerDto save(AssetOwnerDto dto) {
        if (repository.existsByCode(dto.code())) {
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto,
                Entity.getByIdOrNull(jobPositionRepository, dto.jobPositionId(), JobPosition.class),
                Entity.getByIdOrNull(branchRepository, dto.branchId(), Branch.class),
                Entity.getByIdOrNull(departmentRepository, dto.departmentId(), Department.class)
        )));
    }

    @Override
    public AssetOwnerDto update(Long aLong, AssetOwnerDto dto) {
        if (repository.existsByCode(dto.code())) {
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.mergeDto(dto,
                Entity.getById(jobPositionRepository, dto.jobPositionId(), JobPosition.class),
                Entity.getById(branchRepository, dto.branchId(), Branch.class),
                Entity.getById(departmentRepository, dto.departmentId(), Department.class),
                getEntityById(aLong)
        )));
    }

    @Override
    public void delete(Long aLong) {
        repository.delete(getEntityById(aLong));
    }
}
