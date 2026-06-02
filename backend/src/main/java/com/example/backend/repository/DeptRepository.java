package com.example.backend.repository;

import com.example.backend.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Long> {

    List<Dept> findByDelFlag(String delFlag);

    List<Dept> findByParentId(Long parentId);

    List<Dept> findByParentIdAndDelFlag(Long parentId, String delFlag);

    @Query("SELECT d FROM Dept d WHERE d.delFlag = '0' ORDER BY d.sortNum ASC")
    List<Dept> findAllOrderBySort();

    @Query("SELECT d FROM Dept d WHERE d.parentId = 0 AND d.delFlag = '0' ORDER BY d.sortNum ASC")
    List<Dept> findTopLevelDepts();

    boolean existsByDeptName(String deptName);

    boolean existsByDeptNameAndDeptIdNot(String deptName, Long deptId);

    @Query("SELECT COUNT(d) FROM Dept d WHERE d.parentId = ?1 AND d.delFlag = '0'")
    long countByParentId(Long parentId);
}