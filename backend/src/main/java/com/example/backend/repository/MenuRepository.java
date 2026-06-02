package com.example.backend.repository;

import com.example.backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByDelFlag(String delFlag);

    List<Menu> findByParentId(Long parentId);

    List<Menu> findByParentIdAndDelFlag(Long parentId, String delFlag);

    @Query("SELECT m FROM Menu m WHERE m.delFlag = '0' ORDER BY m.sortNum ASC")
    List<Menu> findAllOrderBySort();

    @Query("SELECT m FROM Menu m WHERE m.parentId = 0 AND m.delFlag = '0' ORDER BY m.sortNum ASC")
    List<Menu> findTopLevelMenus();

    boolean existsByMenuName(String menuName);

    boolean existsByMenuNameAndMenuIdNot(String menuName, Long menuId);
}