package com.example.backend.service;

import com.example.backend.entity.Announcement;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    
    List<Announcement> findAll();
    
    Optional<Announcement> findById(Long id);
    
    Announcement create(Announcement announcement);
    
    Announcement update(Long id, Announcement announcement);
    
    void deleteById(Long id);
    
    org.springframework.data.domain.Page<Announcement> searchByTitle(String title, Pageable pageable);
    
    org.springframework.data.domain.Page<Announcement> findActive(Pageable pageable);
}