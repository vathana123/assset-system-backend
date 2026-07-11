package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long>, JpaSpecificationExecutor<JobPosition> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
