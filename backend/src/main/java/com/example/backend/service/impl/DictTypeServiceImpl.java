package com.example.backend.service.impl;

import com.example.backend.entity.DictType;
import com.example.backend.repository.DictTypeRepository;
import com.example.backend.service.DictItemService;
import com.example.backend.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService {

    private final DictTypeRepository dictTypeRepository;
    private final DictItemService dictItemService;

    @Autowired
    public DictTypeServiceImpl(DictTypeRepository dictTypeRepository, DictItemService dictItemService) {
        this.dictTypeRepository = dictTypeRepository;
        this.dictItemService = dictItemService;
    }

    @Override
    public List<DictType> getAllDictTypes() {
        return dictTypeRepository.findAllEnabled();
    }

    @Override
    public DictType getDictTypeById(Long id) {
        return dictTypeRepository.findById(id).orElse(null);
    }

    @Override
    public DictType getDictTypeByCode(String dictCode) {
        return dictTypeRepository.findByDictCode(dictCode).orElse(null);
    }

    @Override
    public DictType createDictType(DictType dictType) {
        if (dictTypeRepository.existsByDictName(dictType.getDictName())) {
            throw new RuntimeException("字典类型名称已存在");
        }
        if (dictTypeRepository.existsByDictCode(dictType.getDictCode())) {
            throw new RuntimeException("字典类型编码已存在");
        }
        return dictTypeRepository.save(dictType);
    }

    @Override
    public DictType updateDictType(DictType dictType) {
        if (dictTypeRepository.existsByDictNameAndDictIdNot(dictType.getDictName(), dictType.getDictId())) {
            throw new RuntimeException("字典类型名称已存在");
        }
        if (dictTypeRepository.existsByDictCodeAndDictIdNot(dictType.getDictCode(), dictType.getDictId())) {
            throw new RuntimeException("字典类型编码已存在");
        }
        DictType existing = dictTypeRepository.findById(dictType.getDictId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("字典类型不存在");
        }
        return dictTypeRepository.save(dictType);
    }

    @Override
    @Transactional
    public void deleteDictType(Long id) {
        DictType dictType = dictTypeRepository.findById(id).orElse(null);
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        dictItemService.deleteDictItemsByDictCode(dictType.getDictCode());
        dictTypeRepository.deleteById(id);
    }

    @Override
    public void initDictTypes() {
        if (dictTypeRepository.findAllEnabled().isEmpty()) {
            DictType statusType = new DictType();
            statusType.setDictName("状态类型");
            statusType.setDictCode("sys_status");
            statusType.setSortNum(1);
            statusType.setStatus("0");
            statusType.setRemark("系统状态字典");
            dictTypeRepository.save(statusType);

            DictType sexType = new DictType();
            sexType.setDictName("性别");
            sexType.setDictCode("sys_sex");
            sexType.setSortNum(2);
            sexType.setStatus("0");
            sexType.setRemark("人员性别字典");
            dictTypeRepository.save(sexType);

            DictType deptType = new DictType();
            deptType.setDictName("部门类型");
            deptType.setDictCode("sys_dept_type");
            deptType.setSortNum(3);
            deptType.setStatus("0");
            deptType.setRemark("部门类型字典");
            dictTypeRepository.save(deptType);

            DictType menuType = new DictType();
            menuType.setDictName("菜单类型");
            menuType.setDictCode("sys_menu_type");
            menuType.setSortNum(4);
            menuType.setStatus("0");
            menuType.setRemark("菜单类型字典");
            dictTypeRepository.save(menuType);
        }
    }
}