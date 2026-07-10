package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {
    Optional<UserInfo> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    @Query("""
            select distinct user
            from UserInfo user
            left join fetch user.roles role
            left join fetch role.permissions
            where user.username = :username
            """)
    Optional<UserInfo> findByUsernameWithRolesAndPermissions(@Param("username") String username);
}
