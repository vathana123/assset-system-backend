package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.ApprovalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApprovalSettingRepository extends JpaRepository<ApprovalSetting, Long>, JpaSpecificationExecutor<ApprovalSetting> {
}
