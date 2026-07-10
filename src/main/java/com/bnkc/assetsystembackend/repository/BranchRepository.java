package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BranchRepository extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {
    boolean existsByCode(String code);
}
