package com.example.backend.controller;

import com.example.backend.entity.LoginUser;
import com.example.backend.entity.LoginResponse;
import com.example.backend.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginUser loginUser) {
        LoginResponse response = authService.login(loginUser);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        LoginResponse response = authService.refreshToken(refreshToken);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "刷新成功");
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/captcha")
    public ResponseEntity<Map<String, Object>> getCaptcha() {
        String captchaData = authService.generateCaptcha();
        String[] parts = captchaData.split(":");
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", Map.of(
            "key", parts[0],
            "captcha", parts[1]
        ));
        return ResponseEntity.ok(result);
    }
}
