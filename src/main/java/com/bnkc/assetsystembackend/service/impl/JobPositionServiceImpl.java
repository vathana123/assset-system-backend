package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.JobPositionDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.JobLevel;
import com.bnkc.assetsystembackend.entity.JobPosition;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.JobPositionMapper;
import com.bnkc.assetsystembackend.repository.JobLevelRepository;
import com.bnkc.assetsystembackend.repository.JobPositionRepository;
import com.bnkc.assetsystembackend.service.JobPositionService;
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
public class JobPositionServiceImpl implements JobPositionService {

    private final JobPositionRepository repository;
    private final JobLevelRepository jobLevelRepository;
    private final JobPositionMapper mapper;

    JobPosition getEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JobPosition.class, id));
    }

    JobLevel getJobLevelById(Long id) {
        return jobLevelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JobLevel.class, id));
    }

    @Override
    public PageResponse<JobPositionDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<JobPosition>builder()
                                .filters(filters)
                                .fields(List.of("code", "name", "shortName", "localName"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public JobPositionDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public JobPositionDto save(JobPositionDto dto) {
        if(repository.existsByCode(dto.code())){
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto, getJobLevelById(dto.jobLevelId()))));
    }

    @Override
    public JobPositionDto update(Long aLong, JobPositionDto dto) {
        if(repository.existsByCode(dto.code())){
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.mergeDto(dto, getJobLevelById(dto.jobLevelId()), getEntityById(aLong))));
    }

    @Override
    public void delete(Long aLong) {
        repository.delete(getEntityById(aLong));
    }
}
