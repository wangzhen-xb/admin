package com.example.backend.service.impl;

import com.example.backend.entity.Announcement;
import com.example.backend.repository.AnnouncementRepository;
import com.example.backend.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAll();
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        return announcementRepository.findById(id);
    }

    @Override
    public Announcement create(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement update(Long id, Announcement announcement) {
        Optional<Announcement> existingOpt = announcementRepository.findById(id);
        if (existingOpt.isPresent()) {
            Announcement existing = existingOpt.get();
            existing.setTitle(announcement.getTitle());
            existing.setContent(announcement.getContent());
            existing.setType(announcement.getType());
            existing.setStatus(announcement.getStatus());
            return announcementRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public List<Announcement> searchByTitle(String title) {
        return announcementRepository.findByTitleContainingOrderByCreateTimeDesc(title);
    }

    @Override
    public List<Announcement> findActive() {
        return announcementRepository.findByStatusOrderByCreateTimeDesc("0");
    }
}