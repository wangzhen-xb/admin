package com.example.backend.repository;

import com.example.backend.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    org.springframework.data.domain.Page<Announcement> findByStatusOrderByCreateTimeDesc(String status, Pageable pageable);
    
    org.springframework.data.domain.Page<Announcement> findByTitleContainingOrderByCreateTimeDesc(String title, Pageable pageable);
}