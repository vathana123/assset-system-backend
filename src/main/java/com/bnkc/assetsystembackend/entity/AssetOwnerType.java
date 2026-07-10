package com.bnkc.assetsystembackend.entity;

import lombok.Getter;

@Getter
public enum AssetOwnerType {
    EMPLOYEE("Employee"),
    ZONE("Zone"),
    Department("Department"),
    BRANCH("Branch");

    private final String label;

    AssetOwnerType(String label) {
        this.label = label;
    }
}
