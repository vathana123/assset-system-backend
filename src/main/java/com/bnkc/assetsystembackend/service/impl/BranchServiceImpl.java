package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.BranchDto;
import com.bnkc.assetsystembackend.data.respone.PageResponse;
import com.bnkc.assetsystembackend.entity.Branch;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.BranchMapper;
import com.bnkc.assetsystembackend.repository.BranchRepository;
import com.bnkc.assetsystembackend.service.BranchService;
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
public class BranchServiceImpl implements BranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;

    Branch getEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Branch.class, id));
    }

    @Override
    public PageResponse<BranchDto> findAll(Map<String, Object> filters, Pageable pageable) {
        return PageResponseMapper
                .toPageResponse(
                        repository.findAll(Search
                                .<Branch>builder()
                                .filters(filters)
                                .fields(List.of("code", "name", "shortName", "localName"))
                                .build(), pageable),
                        mapper::toDto);
    }

    @Override
    public BranchDto findById(Long aLong) {
        return mapper.toDto(getEntityById(aLong));
    }

    @Override
    public BranchDto save(BranchDto dto) {
        if(repository.existsByCode(dto.code())){
            throw new ValidationException("Code %s already exist.".formatted(dto.code()));
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public BranchDto update(Long aLong, BranchDto dto) {
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
