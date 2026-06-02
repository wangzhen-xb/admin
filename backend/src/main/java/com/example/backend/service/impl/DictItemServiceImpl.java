package com.example.backend.service.impl;

import com.example.backend.entity.DictItem;
import com.example.backend.entity.DictType;
import com.example.backend.repository.DictItemRepository;
import com.example.backend.repository.DictTypeRepository;
import com.example.backend.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictItemServiceImpl implements DictItemService {

    private final DictItemRepository dictItemRepository;
    private final DictTypeRepository dictTypeRepository;

    @Autowired
    public DictItemServiceImpl(DictItemRepository dictItemRepository, DictTypeRepository dictTypeRepository) {
        this.dictItemRepository = dictItemRepository;
        this.dictTypeRepository = dictTypeRepository;
    }

    @Override
    public List<DictItem> getDictItemsByDictCode(String dictCode) {
        return dictItemRepository.findByDictCode(dictCode);
    }

    @Override
    public List<DictItem> getEnabledDictItemsByDictCode(String dictCode) {
        return dictItemRepository.findEnabledByDictCode(dictCode);
    }

    @Override
    public DictItem getDictItemById(Long id) {
        return dictItemRepository.findById(id).orElse(null);
    }

    @Override
    public DictItem createDictItem(DictItem dictItem) {
        DictType dictType = dictTypeRepository.findByDictCode(dictItem.getDictCode()).orElse(null);
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        if (dictItemRepository.existsByDictCodeAndItemValue(dictItem.getDictCode(), dictItem.getItemValue())) {
            throw new RuntimeException("字典项值已存在");
        }
        return dictItemRepository.save(dictItem);
    }

    @Override
    public DictItem updateDictItem(DictItem dictItem) {
        DictType dictType = dictTypeRepository.findByDictCode(dictItem.getDictCode()).orElse(null);
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        if (dictItemRepository.existsByDictCodeAndItemValueAndItemIdNot(dictItem.getDictCode(), dictItem.getItemValue(), dictItem.getItemId())) {
            throw new RuntimeException("字典项值已存在");
        }
        DictItem existing = dictItemRepository.findById(dictItem.getItemId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("字典项不存在");
        }
        return dictItemRepository.save(dictItem);
    }

    @Override
    public void deleteDictItem(Long id) {
        DictItem dictItem = dictItemRepository.findById(id).orElse(null);
        if (dictItem == null) {
            throw new RuntimeException("字典项不存在");
        }
        dictItemRepository.deleteById(id);
    }

    @Override
    public void deleteDictItemsByDictCode(String dictCode) {
        dictItemRepository.deleteByDictCode(dictCode);
    }
}