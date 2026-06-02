package com.example.backend.service;

import com.example.backend.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    Role createRole(Role role);

    Role updateRole(Role role);

    void deleteRole(Long id);

    void initRoles();
}