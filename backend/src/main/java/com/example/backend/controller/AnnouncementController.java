package com.example.backend.controller;

import com.example.backend.entity.Announcement;
import com.example.backend.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAnnouncements(
            @RequestParam(required = false) String title) {
        
        Map<String, Object> result = new HashMap<>();
        List<Announcement> announcements;

        if (title != null && !title.isEmpty()) {
            announcements = announcementService.searchByTitle(title);
        } else {
            announcements = announcementService.findAll();
        }

        result.put("code", 200);
        result.put("message", "success");
        result.put("data", announcements);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAnnouncementById(@PathVariable Long id) {
        Optional<Announcement> announcementOpt = announcementService.findById(id);
        Map<String, Object> result = new HashMap<>();
        if (announcementOpt.isPresent()) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", announcementOpt.get());
        } else {
            result.put("code", 404);
            result.put("message", "公告不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAnnouncement(@RequestBody Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        try {
            Announcement saved = announcementService.create(announcement);
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
    public ResponseEntity<Map<String, Object>> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        try {
            Announcement updated = announcementService.update(id, announcement);
            if (updated == null) {
                result.put("code", 404);
                result.put("message", "公告不存在");
                return ResponseEntity.ok(result);
            }
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAnnouncement(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Announcement> announcementOpt = announcementService.findById(id);
            if (!announcementOpt.isPresent()) {
                result.put("code", 404);
                result.put("message", "公告不存在");
                return ResponseEntity.ok(result);
            }
            announcementService.deleteById(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveAnnouncements() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Announcement> announcements = announcementService.findActive();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", announcements);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}