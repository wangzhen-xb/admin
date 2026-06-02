package com.example.backend.service.impl;

import com.example.backend.entity.Dept;
import com.example.backend.repository.DeptRepository;
import com.example.backend.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    private final DeptRepository deptRepository;

    @Autowired
    public DeptServiceImpl(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    @Override
    public List<Dept> getAllDepts() {
        return deptRepository.findAllOrderBySort();
    }

    @Override
    public List<Dept> getDeptsTree() {
        List<Dept> allDepts = deptRepository.findAllOrderBySort();
        return buildTree(allDepts, 0L);
    }

    private List<Dept> buildTree(List<Dept> depts, Long parentId) {
        return depts.stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .collect(Collectors.toList());
    }

    @Override
    public Dept getDeptById(Long id) {
        return deptRepository.findById(id).orElse(null);
    }

    @Override
    public Dept createDept(Dept dept) {
        if (deptRepository.existsByDeptName(dept.getDeptName())) {
            throw new RuntimeException("部门名称已存在");
        }
        return deptRepository.save(dept);
    }

    @Override
    public Dept updateDept(Dept dept) {
        if (deptRepository.existsByDeptNameAndDeptIdNot(dept.getDeptName(), dept.getDeptId())) {
            throw new RuntimeException("部门名称已存在");
        }
        Dept existing = deptRepository.findById(dept.getDeptId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("部门不存在");
        }
        return deptRepository.save(dept);
    }

    @Override
    public void deleteDept(Long id) {
        Dept dept = deptRepository.findById(id).orElse(null);
        if (dept == null) {
            throw new RuntimeException("部门不存在");
        }
        long childrenCount = deptRepository.countByParentId(id);
        if (childrenCount > 0) {
            throw new RuntimeException("请先删除子部门");
        }
        dept.setDelFlag("1");
        deptRepository.save(dept);
    }

    @Override
    public void initDepts() {
        if (deptRepository.findTopLevelDepts().isEmpty()) {
            Dept root = new Dept();
            root.setParentId(0L);
            root.setDeptName("组织结构");
            root.setSortNum(1);
            root.setStatus("0");
            deptRepository.save(root);

            Dept dept1 = new Dept();
            dept1.setParentId(root.getDeptId());
            dept1.setDeptName("技术部");
            dept1.setLeader("张三");
            dept1.setPhone("13800138001");
            dept1.setEmail("tech@example.com");
            dept1.setSortNum(1);
            dept1.setStatus("0");
            deptRepository.save(dept1);

            Dept dept2 = new Dept();
            dept2.setParentId(root.getDeptId());
            dept2.setDeptName("产品部");
            dept2.setLeader("李四");
            dept2.setPhone("13800138002");
            dept2.setEmail("product@example.com");
            dept2.setSortNum(2);
            dept2.setStatus("0");
            deptRepository.save(dept2);

            Dept dept3 = new Dept();
            dept3.setParentId(root.getDeptId());
            dept3.setDeptName("运营部");
            dept3.setLeader("王五");
            dept3.setPhone("13800138003");
            dept3.setEmail("ops@example.com");
            dept3.setSortNum(3);
            dept3.setStatus("0");
            deptRepository.save(dept3);
        }
    }
}