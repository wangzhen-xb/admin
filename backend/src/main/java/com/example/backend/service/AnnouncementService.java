package com.example.backend.service;

import com.example.backend.entity.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    
    List<Announcement> findAll();
    
    Optional<Announcement> findById(Long id);
    
    Announcement create(Announcement announcement);
    
    Announcement update(Long id, Announcement announcement);
    
    void deleteById(Long id);
    
    List<Announcement> searchByTitle(String title);
    
    List<Announcement> findActive();
}