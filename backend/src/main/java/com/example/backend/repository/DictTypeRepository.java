package com.example.backend.repository;

import com.example.backend.entity.DictType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictTypeRepository extends JpaRepository<DictType, Long> {

    List<DictType> findByStatus(String status);

    Optional<DictType> findByDictCode(String dictCode);

    boolean existsByDictName(String dictName);

    boolean existsByDictNameAndDictIdNot(String dictName, Long dictId);

    boolean existsByDictCode(String dictCode);

    boolean existsByDictCodeAndDictIdNot(String dictCode, Long dictId);

    @Query("SELECT d FROM DictType d WHERE d.status = '0' ORDER BY d.sortNum ASC")
    List<DictType> findAllEnabled();
}