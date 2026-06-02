package com.example.backend.repository;

import com.example.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByDelFlag(String delFlag);

    Role findByRoleKey(String roleKey);

    boolean existsByRoleKey(String roleKey);

    boolean existsByRoleKeyAndRoleIdNot(String roleKey, Long roleId);

    @Query("SELECT r FROM Role r WHERE r.delFlag = '0' ORDER BY r.roleSort ASC")
    List<Role> findAllOrderBySort();
}