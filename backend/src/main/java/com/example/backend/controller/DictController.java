package com.example.backend.controller;

import com.example.backend.entity.DictItem;
import com.example.backend.entity.DictType;
import com.example.backend.service.DictItemService;
import com.example.backend.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/dict")
public class DictController {

    private final DictTypeService dictTypeService;
    private final DictItemService dictItemService;

    @Autowired
    public DictController(DictTypeService dictTypeService, DictItemService dictItemService) {
        this.dictTypeService = dictTypeService;
        this.dictItemService = dictItemService;
    }

    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getAllDictTypes() {
        List<DictType> dictTypes = dictTypeService.getAllDictTypes();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", dictTypes);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<Map<String, Object>> getDictTypeById(@PathVariable Long id) {
        DictType dictType = dictTypeService.getDictTypeById(id);
        Map<String, Object> result = new HashMap<>();
        if (dictType != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", dictType);
        } else {
            result.put("code", 404);
            result.put("message", "字典类型不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/types")
    public ResponseEntity<Map<String, Object>> createDictType(@RequestBody DictType dictType) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictType saved = dictTypeService.createDictType(dictType);
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<Map<String, Object>> updateDictType(@PathVariable Long id, @RequestBody DictType dictType) {
        dictType.setDictId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            DictType updated = dictTypeService.updateDictType(dictType);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<Map<String, Object>> deleteDictType(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            dictTypeService.deleteDictType(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/items/{dictCode}")
    public ResponseEntity<Map<String, Object>> getDictItems(@PathVariable String dictCode) {
        List<DictItem> dictItems = dictItemService.getDictItemsByDictCode(dictCode);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", dictItems);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/items/{dictCode}/enabled")
    public ResponseEntity<Map<String, Object>> getEnabledDictItems(@PathVariable String dictCode) {
        List<DictItem> dictItems = dictItemService.getEnabledDictItemsByDictCode(dictCode);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", dictItems);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Map<String, Object>> getDictItemById(@PathVariable Long id) {
        DictItem dictItem = dictItemService.getDictItemById(id);
        Map<String, Object> result = new HashMap<>();
        if (dictItem != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", dictItem);
        } else {
            result.put("code", 404);
            result.put("message", "字典项不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/items")
    public ResponseEntity<Map<String, Object>> createDictItem(@RequestBody DictItem dictItem) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictItem saved = dictItemService.createDictItem(dictItem);
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Map<String, Object>> updateDictItem(@PathVariable Long id, @RequestBody DictItem dictItem) {
        dictItem.setItemId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            DictItem updated = dictItemService.updateDictItem(dictItem);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Map<String, Object>> deleteDictItem(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            dictItemService.deleteDictItem(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}