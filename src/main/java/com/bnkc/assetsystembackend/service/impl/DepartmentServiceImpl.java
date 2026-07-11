package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.DepartmentDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.Department;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.DepartmentMapper;
import com.bnkc.assetsystembackend.repository.DepartmentRepository;
import com.bnkc.assetsystembackend.service.DepartmentService;
import com.bnkc.assetsystembackend.specification.Search;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    Department getEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Department.class, id));
    }

    @Override
    public PageResponse<DepartmentDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<Department>builder()
                                .filters(filters)
                                .fields(List.of("code", "name", "shortName", "localName"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public DepartmentDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public DepartmentDto save(DepartmentDto dto) {
        if(repository.existsByCode(dto.code())){
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public DepartmentDto update(Long aLong, DepartmentDto dto) {
        if (repository.existsByCodeAndIdNot(dto.code(), dto.id())) {
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.mergeDto(dto, getEntityById(aLong))));
    }

    @Override
    public void delete(Long aLong) {
        repository.delete(getEntityById(aLong));
    }
}
