package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.JobLevelDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.JobLevel;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.JobLevelMapper;
import com.bnkc.assetsystembackend.repository.JobLevelRepository;
import com.bnkc.assetsystembackend.service.JobLevelService;
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
public class JobLevelServiceImpl implements JobLevelService {

    private final JobLevelRepository repository;
    private final JobLevelMapper mapper;

    JobLevel getEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JobLevel.class, id));
    }

    @Override
    public PageResponse<JobLevelDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<JobLevel>builder()
                                .filters(filters)
                                .fields(List.of("code", "name", "shortName", "localName"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public JobLevelDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public JobLevelDto save(JobLevelDto dto) {
        if(repository.existsByCode(dto.code())){
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public JobLevelDto update(Long aLong, JobLevelDto dto) {
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
