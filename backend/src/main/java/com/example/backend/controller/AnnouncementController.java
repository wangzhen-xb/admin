package com.example.backend.controller;

import com.example.backend.entity.Announcement;
import com.example.backend.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Announcement> announcementPage;


        if (title != null && !title.isEmpty()) {
            announcementPage = announcementService.searchByTitle(title, pageable);
        } else {
            announcementPage = announcementService.findActive(pageable);
        }
        List<Announcement> announcementsList = announcementPage.getContent();

        Map<String, Object> result = new HashMap<>();
        result.put("list", announcementsList);
        result.put("total", announcementPage.getTotalElements());
        result.put("page", announcementPage.getNumber());
        result.put("size", announcementPage.getSize());


        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", result);
        response.put("message", "success");
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Map<String, Object>> getActiveAnnouncements(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
            Page<Announcement> announcements = announcementService.findActive(pageable);
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