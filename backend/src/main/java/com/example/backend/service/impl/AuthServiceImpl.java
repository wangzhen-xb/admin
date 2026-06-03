package com.example.backend.service.impl;

import com.example.backend.entity.LoginResponse;
import com.example.backend.entity.LoginUser;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        initAdminUser();
    }

    @Override
    public LoginResponse login(LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        String captcha = loginUser.getCaptcha();

        if (!validateCaptcha(captcha)) {
            throw new RuntimeException("验证码错误");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户名不存在");
        }
        
        User user = userOpt.get();
        
        if (!"0".equals(user.getStatus())) {
            throw new RuntimeException("用户已禁用");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getUsername());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId(), user.getUsername());
        return new LoginResponse(accessToken, refreshToken, user);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtTokenUtil.validateToken(refreshToken) || !jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("刷新令牌无效或已过期");
        }

        Long userId = jwtTokenUtil.getUserIdFromToken(refreshToken);
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        if (userId == null || username == null) {
            throw new RuntimeException("刷新令牌无效");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }

        String newAccessToken = jwtTokenUtil.generateAccessToken(userId, username);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userId, username);
        return new LoginResponse(newAccessToken, newRefreshToken, userOpt.get());
    }

    @Override
    public String generateCaptcha() {
        String captcha = generateRandomCaptcha();
        String key = UUID.randomUUID().toString();
        captchaStore.put(key, captcha);
        return key + ":" + captcha;
    }

    @Override
    public boolean validateCaptcha(String captcha) {
        if (captcha == null || captcha.isEmpty()) {
            throw new RuntimeException("验证码不能为空");
        }
        
        String[] parts = captcha.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("验证码格式错误");
        }
        
        String key = parts[0];
        String expectedCaptcha = parts[1];
        
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("验证码 key 为空");
        }
        
        String storedCaptcha = captchaStore.get(key);
        if (storedCaptcha == null) {
            throw new RuntimeException("验证码已过期或不存在");
        }
        
        captchaStore.remove(key);
        
        if (!storedCaptcha.equalsIgnoreCase(expectedCaptcha)) {
            throw new RuntimeException("验证码错误");
        }
        
        return true;
    }

    private String generateRandomCaptcha() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    @Override
    public void initAdminUser() {
        Optional<User> existingOpt = userRepository.findByUsername("admin");
        User admin;
        if (existingOpt.isPresent()) {
            admin = existingOpt.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
        } else {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setPhone("13800138000");
            admin.setStatus("0");
            admin.setDelFlag("0");
        }
        userRepository.save(admin);
    }
}
