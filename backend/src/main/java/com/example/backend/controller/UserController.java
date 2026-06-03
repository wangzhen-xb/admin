package com.example.backend.controller;

import com.example.backend.entity.Dept;
import com.example.backend.entity.User;
import com.example.backend.repository.DeptRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final DeptRepository deptRepository;

    @Autowired
    public UserController(UserRepository userRepository, DeptRepository deptRepository) {
        this.userRepository = userRepository;
        this.deptRepository = deptRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<User> userPage;

        if ((username != null && !username.isEmpty()) || (email != null && !email.isEmpty())) {
            String u = username != null ? username : "";
            String e = email != null ? email : "";
            userPage = userRepository.findByUsernameOrEmail(u, e, pageable);
        } else {
            userPage = userRepository.findAllActive(pageable);
        }

        List<User> users = userPage.getContent();
        users.forEach(user -> {
            if (user.getDeptId() != null) {
                Optional<Dept> dept = deptRepository.findById(user.getDeptId());
                dept.ifPresent(d -> user.setDeptName(d.getDeptName()));
            }
        });

        Map<String, Object> data = new HashMap<>();
        data.put("list", users);
        data.put("total", userPage.getTotalElements());
        data.put("page", userPage.getNumber());
        data.put("size", userPage.getSize());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        Map<String, Object> result = new HashMap<>();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getDeptId() != null) {
                Optional<Dept> dept = deptRepository.findById(user.getDeptId());
                dept.ifPresent(d -> user.setDeptName(d.getDeptName()));
            }
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", user);
        } else {
            result.put("code", 404);
            result.put("message", "用户不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                result.put("code", 500);
                result.put("message", "用户名已存在");
                return ResponseEntity.ok(result);
            }
            User saved = userRepository.save(user);
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
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> existingOpt = userRepository.findById(id);
            if (!existingOpt.isPresent()) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return ResponseEntity.ok(result);
            }
            if (userRepository.existsByUsernameAndUserIdNot(user.getUsername(), id)) {
                result.put("code", 500);
                result.put("message", "用户名已存在");
                return ResponseEntity.ok(result);
            }
            user.setUserId(id);
            User saved = userRepository.save(user);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return ResponseEntity.ok(result);
            }
            User user = userOpt.get();
            user.setDelFlag("1");
            userRepository.save(user);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}