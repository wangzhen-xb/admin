package com.example.backend.service.impl;

import com.example.backend.entity.Role;
import com.example.backend.repository.RoleRepository;
import com.example.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAllOrderBySort();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role createRole(Role role) {
        if (roleRepository.existsByRoleKey(role.getRoleKey())) {
            throw new RuntimeException("角色标识已存在");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Role role) {
        if (roleRepository.existsByRoleKeyAndRoleIdNot(role.getRoleKey(), role.getRoleId())) {
            throw new RuntimeException("角色标识已存在");
        }
        Role existing = roleRepository.findById(role.getRoleId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("角色不存在");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        role.setDelFlag("1");
        roleRepository.save(role);
    }

    @Override
    public void initRoles() {
        if (!roleRepository.existsByRoleKey("admin")) {
            Role adminRole = new Role();
            adminRole.setRoleName("管理员");
            adminRole.setRoleKey("admin");
            adminRole.setRoleSort(1);
            adminRole.setStatus("0");
            adminRole.setRemark("系统管理员");
            roleRepository.save(adminRole);
        }
        if (!roleRepository.existsByRoleKey("user")) {
            Role userRole = new Role();
            userRole.setRoleName("普通用户");
            userRole.setRoleKey("user");
            userRole.setRoleSort(2);
            userRole.setStatus("0");
            userRole.setRemark("普通用户权限");
            roleRepository.save(userRole);
        }
    }
}