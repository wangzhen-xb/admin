package com.example.backend.repository;

import com.example.backend.entity.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictItemRepository extends JpaRepository<DictItem, Long> {

    List<DictItem> findByDictCode(String dictCode);

    List<DictItem> findByDictCodeAndStatus(String dictCode, String status);

    @Query("SELECT d FROM DictItem d WHERE d.dictCode = ?1 AND d.status = '0' ORDER BY d.sortNum ASC")
    List<DictItem> findEnabledByDictCode(String dictCode);

    boolean existsByDictCodeAndItemValue(String dictCode, String itemValue);

    boolean existsByDictCodeAndItemValueAndItemIdNot(String dictCode, String itemValue, Long itemId);

    long countByDictCode(String dictCode);

    void deleteByDictCode(String dictCode);
}