package com.example.backend.controller;

import com.example.backend.entity.Dept;
import com.example.backend.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/depts")
public class DeptController {

    private final DeptService deptService;

    @Autowired
    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDepts() {
        List<Dept> depts = deptService.getAllDepts();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", depts);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tree")
    public ResponseEntity<Map<String, Object>> getDeptsTree() {
        List<Dept> depts = deptService.getDeptsTree();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", depts);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDeptById(@PathVariable Long id) {
        Dept dept = deptService.getDeptById(id);
        Map<String, Object> result = new HashMap<>();
        if (dept != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", dept);
        } else {
            result.put("code", 404);
            result.put("message", "部门不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDept(@RequestBody Dept dept) {
        Map<String, Object> result = new HashMap<>();
        try {
            Dept saved = deptService.createDept(dept);
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDept(@PathVariable Long id, @RequestBody Dept dept) {
        dept.setDeptId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            Dept updated = deptService.updateDept(dept);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDept(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            deptService.deleteDept(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}