package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>,
        JpaSpecificationExecutor<ApprovalLine> {

    @Query("""
        SELECT COALESCE(MAX(a.lineOrder), 0)
        FROM ApprovalLine a
        WHERE a.approvalSetting.id = :approvalSettingId
    """)
    Integer getMaxOrderByApprovalSettingId(Long approvalSettingId);

    Optional<ApprovalLine> findFirstByApprovalSetting_IdOrderByLineOrderAsc(Long approvalSettingId);
}
