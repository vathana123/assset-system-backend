package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.JobLevelDto;
import com.bnkc.assetsystembackend.entity.JobLevel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobLevelMapper {
    JobLevelDto toDto(JobLevel entity);
    List<JobLevelDto> toDto(List<JobLevel> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    JobLevel toEntity(JobLevelDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    JobLevel mergeDto(JobLevelDto dto, @MappingTarget JobLevel entity);
}
