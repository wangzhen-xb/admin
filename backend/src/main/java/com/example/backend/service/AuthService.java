package com.example.backend.service;

import com.example.backend.entity.LoginUser;
import com.example.backend.entity.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginUser loginUser);
    String generateCaptcha();
    boolean validateCaptcha(String captcha);
    void initAdminUser();
}