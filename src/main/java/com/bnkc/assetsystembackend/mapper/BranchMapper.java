package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.BranchDto;
import com.bnkc.assetsystembackend.entity.Branch;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchDto toDto(Branch entity);
    List<BranchDto> toDto(List<Branch> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Branch toEntity(BranchDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Branch mergeDto(BranchDto dto, @MappingTarget Branch entity);
}
