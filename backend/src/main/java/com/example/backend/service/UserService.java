package com.example.backend.service;

import com.example.backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> findAll();
    
    Optional<User> findById(Long id);
    
    User create(User user);
    
    User update(Long id, User user);
    
    void deleteById(Long id);
    
    List<User> searchByName(String name);
}