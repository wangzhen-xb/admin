package com.example.backend.service;

import com.example.backend.entity.DictType;

import java.util.List;

public interface DictTypeService {

    List<DictType> getAllDictTypes();

    DictType getDictTypeById(Long id);

    DictType getDictTypeByCode(String dictCode);

    DictType createDictType(DictType dictType);

    DictType updateDictType(DictType dictType);

    void deleteDictType(Long id);

    void initDictTypes();
}