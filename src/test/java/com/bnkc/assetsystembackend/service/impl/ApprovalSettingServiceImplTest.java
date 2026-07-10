package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.dto.ApprovalLineDto;
import com.bnkc.assetsystembackend.data.dto.ApprovalSettingDto;
import com.bnkc.assetsystembackend.entity.ApprovalLine;
import com.bnkc.assetsystembackend.entity.ApprovalSetting;
import com.bnkc.assetsystembackend.entity.Branch;
import com.bnkc.assetsystembackend.entity.Department;
import com.bnkc.assetsystembackend.entity.JobPosition;
import com.bnkc.assetsystembackend.entity.Role;
import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import com.bnkc.assetsystembackend.mapper.ApprovalSettingMapper;
import com.bnkc.assetsystembackend.repository.ApprovalSettingRepository;
import com.bnkc.assetsystembackend.repository.BranchRepository;
import com.bnkc.assetsystembackend.repository.DepartmentRepository;
import com.bnkc.assetsystembackend.repository.JobPositionRepository;
import com.bnkc.assetsystembackend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApprovalSettingServiceImplTest {

    @Mock
    private ApprovalSettingRepository repository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private JobPositionRepository jobPositionRepository;

    private ApprovalSettingServiceImpl service;

    @BeforeEach
    void setUp() {
        ApprovalSettingMapper mapper = Mappers.getMapper(ApprovalSettingMapper.class);
        service = new ApprovalSettingServiceImpl(
                repository,
                roleRepository,
                branchRepository,
                departmentRepository,
                jobPositionRepository,
                mapper);
    }

    @Test
    void saveRecalculatesLineOrderAndLoadsRelatedEntities() {
        Role role = Role.builder().id(1L).name("Manager").build();
        Branch branch = Branch.builder().id(2L).name("Head Office").build();
        Department department = Department.builder().id(3L).name("Finance").build();
        JobPosition jobPosition = JobPosition.builder().id(4L).name("Supervisor").build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(branchRepository.findById(2L)).thenReturn(Optional.of(branch));
        when(departmentRepository.findById(3L)).thenReturn(Optional.of(department));
        when(jobPositionRepository.findById(4L)).thenReturn(Optional.of(jobPosition));
        when(repository.save(org.mockito.ArgumentMatchers.any(ApprovalSetting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApprovalLineDto first = lineDto(null, "First", 99, 1L, 2L, 3L, 4L);
        ApprovalLineDto second = lineDto(null, "Second", -10, null, null, null, null);

        ApprovalSettingDto result = service.save(new ApprovalSettingDto(
                null, "Asset Request", List.of(first, second)));

        assertThat(result.approvalLines())
                .extracting(ApprovalLineDto::lineOrder)
                .containsExactly(1, 2);

        ApprovalSetting saved = captureSavedSetting();
        assertThat(saved.getApprovalLines()).hasSize(2);
        assertThat(saved.getApprovalLines().get(0).getApprovalSetting()).isSameAs(saved);
        assertThat(saved.getApprovalLines().get(0).getRole()).isSameAs(role);
        assertThat(saved.getApprovalLines().get(0).getBranch()).isSameAs(branch);
        assertThat(saved.getApprovalLines().get(0).getDepartment()).isSameAs(department);
        assertThat(saved.getApprovalLines().get(0).getJobPosition()).isSameAs(jobPosition);

        verify(roleRepository).findById(1L);
        verify(branchRepository).findById(2L);
        verify(departmentRepository).findById(3L);
        verify(jobPositionRepository).findById(4L);
    }

    @Test
    void updateSynchronizesReordersAndRemovesOmittedLines() {
        ApprovalSetting setting = ApprovalSetting.builder()
                .id(100L)
                .name("Old Name")
                .approvalLines(new ArrayList<>())
                .build();

        ApprovalLine omitted = existingLine(10L, "Omitted", 1, setting);
        ApprovalLine retained = existingLine(11L, "Retained", 2, setting);
        setting.addApprovalLine(omitted);
        setting.addApprovalLine(retained);

        when(repository.findById(100L)).thenReturn(Optional.of(setting));
        when(repository.save(setting)).thenReturn(setting);

        ApprovalLineDto retainedDto = lineDto(11L, "Updated", 500, null, null, null, null);
        ApprovalLineDto newDto = lineDto(null, "New", 100, null, null, null, null);

        ApprovalSettingDto result = service.update(100L, new ApprovalSettingDto(
                999L, "New Name", List.of(retainedDto, newDto)));

        assertThat(setting.getName()).isEqualTo("New Name");
        assertThat(setting.getApprovalLines())
                .extracting(ApprovalLine::getId)
                .containsExactly(11L, null);
        assertThat(setting.getApprovalLines())
                .extracting(ApprovalLine::getLineOrder)
                .containsExactly(1, 2);
        assertThat(setting.getApprovalLines().get(0).getName()).isEqualTo("Updated");
        assertThat(result.approvalLines())
                .extracting(ApprovalLineDto::name)
                .containsExactly("Updated", "New");
    }

    @Test
    void updateRejectsLineThatDoesNotBelongToSetting() {
        ApprovalSetting setting = ApprovalSetting.builder()
                .id(100L)
                .name("Setting")
                .approvalLines(new ArrayList<>())
                .build();

        when(repository.findById(100L)).thenReturn(Optional.of(setting));

        ApprovalSettingDto dto = new ApprovalSettingDto(
                null,
                "Setting",
                List.of(lineDto(999L, "Foreign", 1, null, null, null, null)));

        assertThatThrownBy(() -> service.update(100L, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("ApprovalLine with ID 999 not found.");
    }

    @Test
    void updateCanClearOptionalRelationships() {
        ApprovalSetting setting = ApprovalSetting.builder()
                .id(100L)
                .name("Setting")
                .approvalLines(new ArrayList<>())
                .build();

        ApprovalLine line = existingLine(10L, "Line", 1, setting);
        line.setRole(Role.builder().id(1L).build());
        line.setBranch(Branch.builder().id(2L).build());
        line.setDepartment(Department.builder().id(3L).build());
        line.setJobPosition(JobPosition.builder().id(4L).build());
        setting.addApprovalLine(line);

        when(repository.findById(100L)).thenReturn(Optional.of(setting));
        when(repository.save(setting)).thenReturn(setting);

        service.update(100L, new ApprovalSettingDto(
                null,
                "Setting",
                List.of(lineDto(10L, "Line", 1, null, null, null, null))));

        assertThat(line.getRole()).isNull();
        assertThat(line.getBranch()).isNull();
        assertThat(line.getDepartment()).isNull();
        assertThat(line.getJobPosition()).isNull();
    }

    private ApprovalSetting captureSavedSetting() {
        org.mockito.ArgumentCaptor<ApprovalSetting> captor =
                org.mockito.ArgumentCaptor.forClass(ApprovalSetting.class);
        verify(repository).save(captor.capture());
        return captor.getValue();
    }

    private ApprovalLine existingLine(Long id,
                                      String name,
                                      Integer lineOrder,
                                      ApprovalSetting setting) {
        return ApprovalLine.builder()
                .id(id)
                .name(name)
                .label(name + " Label")
                .action("APPROVE")
                .lineOrder(lineOrder)
                .approvalSetting(setting)
                .build();
    }

    private ApprovalLineDto lineDto(Long id,
                                    String name,
                                    Integer lineOrder,
                                    Long roleId,
                                    Long branchId,
                                    Long departmentId,
                                    Long jobPositionId) {
        return new ApprovalLineDto(
                id,
                name,
                name + " Label",
                "APPROVE",
                lineOrder,
                roleId,
                null,
                branchId,
                null,
                null,
                false,
                departmentId,
                null,
                null,
                false,
                jobPositionId,
                null,
                null);
    }
}
