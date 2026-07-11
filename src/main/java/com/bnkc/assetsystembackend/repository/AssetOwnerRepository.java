package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.AssetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AssetOwnerRepository extends JpaRepository<AssetOwner, Long>, JpaSpecificationExecutor<AssetOwner> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
