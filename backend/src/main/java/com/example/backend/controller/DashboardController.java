package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.DeptRepository;
import com.example.backend.repository.MenuRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final DeptRepository deptRepository;

    @Autowired
    public DashboardController(UserRepository userRepository, RoleRepository roleRepository,
                              MenuRepository menuRepository, DeptRepository deptRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.deptRepository = deptRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            long userCount = userRepository.countByDelFlag("0");
            long roleCount = roleRepository.count();
            long menuCount = menuRepository.count();
            long deptCount = deptRepository.count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userCount);
            stats.put("totalRoles", roleCount);
            stats.put("totalMenus", menuCount);
            stats.put("totalDepts", deptCount);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", stats);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent-users")
    public ResponseEntity<Map<String, Object>> getRecentUsers() {
        Map<String, Object> result = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createTime"));
            List<User> users = userRepository.findAllActive(pageable).getContent();

            List<Map<String, Object>> userList = new ArrayList<>();
            users.forEach(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getUserId());
                userMap.put("name", user.getUsername());
                userMap.put("role", "用户");
                if (user.getCreateTime() != null) {
                    userMap.put("createTime", user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                } else {
                    userMap.put("createTime", "");
                }
                userList.add(userMap);
            });

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", userList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> infoList = new ArrayList<>();
            
            Map<String, Object> version = new HashMap<>();
            version.put("label", "系统版本");
            version.put("value", "v1.0.0");
            infoList.add(version);

            Map<String, Object> uptime = new HashMap<>();
            uptime.put("label", "运行时间");
            uptime.put("value", "128 天");
            infoList.add(uptime);

            long onlineCount = userRepository.countByDelFlag("0");
            Map<String, Object> onlineUsers = new HashMap<>();
            onlineUsers.put("label", "在线用户");
            onlineUsers.put("value", onlineCount + "");
            infoList.add(onlineUsers);

            Map<String, Object> load = new HashMap<>();
            load.put("label", "服务器负载");
            load.put("value", "35%");
            infoList.add(load);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", infoList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activity-log")
    public ResponseEntity<Map<String, Object>> getActivityLog() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> activities = new ArrayList<>();

            Map<String, Object> activity1 = new HashMap<>();
            activity1.put("icon", "Activity");
            activity1.put("color", "green");
            activity1.put("label", "用户登录");
            activity1.put("user", "admin");
            activity1.put("time", "5分钟前");
            activities.add(activity1);

            Map<String, Object> activity2 = new HashMap<>();
            activity2.put("icon", "FolderOpen");
            activity2.put("color", "blue");
            activity2.put("label", "创建资源");
            activity2.put("user", "张三");
            activity2.put("time", "30分钟前");
            activities.add(activity2);

            Map<String, Object> activity3 = new HashMap<>();
            activity3.put("icon", "Clock");
            activity3.put("color", "orange");
            activity3.put("label", "更新信息");
            activity3.put("user", "李四");
            activity3.put("time", "1小时前");
            activities.add(activity3);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", activities);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}