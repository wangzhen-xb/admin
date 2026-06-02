package com.example.backend.controller;

import com.example.backend.entity.Menu;
import com.example.backend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", menus);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tree")
    public ResponseEntity<Map<String, Object>> getMenusTree() {
        List<Menu> menus = menuService.getMenusTree();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", menus);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        Map<String, Object> result = new HashMap<>();
        if (menu != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", menu);
        } else {
            result.put("code", 404);
            result.put("message", "菜单不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createMenu(@RequestBody Menu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            Menu saved = menuService.createMenu(menu);
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
    public ResponseEntity<Map<String, Object>> updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setMenuId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            Menu updated = menuService.updateMenu(menu);
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
    public ResponseEntity<Map<String, Object>> deleteMenu(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            menuService.deleteMenu(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}