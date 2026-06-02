package com.example.backend.service;

import com.example.backend.entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getAllMenus();

    List<Menu> getMenusTree();

    Menu getMenuById(Long id);

    Menu createMenu(Menu menu);

    Menu updateMenu(Menu menu);

    void deleteMenu(Long id);

    void initMenus();
}