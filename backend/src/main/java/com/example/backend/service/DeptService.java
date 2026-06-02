package com.example.backend.service;

import com.example.backend.entity.Dept;

import java.util.List;

public interface DeptService {

    List<Dept> getAllDepts();

    List<Dept> getDeptsTree();

    Dept getDeptById(Long id);

    Dept createDept(Dept dept);

    Dept updateDept(Dept dept);

    void deleteDept(Long id);

    void initDepts();
}