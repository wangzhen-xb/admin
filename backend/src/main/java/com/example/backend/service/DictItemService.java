package com.example.backend.service;

import com.example.backend.entity.DictItem;

import java.util.List;

public interface DictItemService {

    List<DictItem> getDictItemsByDictCode(String dictCode);

    List<DictItem> getEnabledDictItemsByDictCode(String dictCode);

    DictItem getDictItemById(Long id);

    DictItem createDictItem(DictItem dictItem);

    DictItem updateDictItem(DictItem dictItem);

    void deleteDictItem(Long id);

    void deleteDictItemsByDictCode(String dictCode);
}