package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    boolean existsByCode(String code);
}
