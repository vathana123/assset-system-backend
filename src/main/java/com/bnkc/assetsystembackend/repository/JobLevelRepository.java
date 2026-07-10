package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobLevelRepository extends JpaRepository<JobLevel, Long>, JpaSpecificationExecutor<JobLevel> {
}
