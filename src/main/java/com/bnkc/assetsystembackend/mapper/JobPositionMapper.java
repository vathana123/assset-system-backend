package com.bnkc.assetsystembackend.mapper;

import com.bnkc.assetsystembackend.data.dto.JobPositionDto;
import com.bnkc.assetsystembackend.entity.JobLevel;
import com.bnkc.assetsystembackend.entity.JobPosition;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    @Mapping(source = "jobLevel.id", target = "jobLevelId")
    @Mapping(source = "jobLevel.code", target = "jobLevelCode")
    @Mapping(source = "jobLevel.name", target = "jobLevelName")
    JobPositionDto toDto(JobPosition entity);

    List<JobPositionDto> toDto(List<JobPosition> entities);

    @Mapping(source = "dto.code", target = "code")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.shortName", target = "shortName")
    @Mapping(source = "dto.localName", target = "localName")
    @Mapping(source = "jobLevel", target = "jobLevel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    JobPosition toEntity(JobPositionDto dto, JobLevel jobLevel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.code", target = "code")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.shortName", target = "shortName")
    @Mapping(source = "dto.localName", target = "localName")
    @Mapping(source = "jobLevel", target = "jobLevel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    JobPosition mergeDto(JobPositionDto dto,
                         JobLevel jobLevel,
                         @MappingTarget JobPosition entity);
}
