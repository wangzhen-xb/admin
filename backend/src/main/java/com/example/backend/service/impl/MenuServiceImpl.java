package com.example.backend.service.impl;

import com.example.backend.entity.Menu;
import com.example.backend.repository.MenuRepository;
import com.example.backend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAllOrderBySort();
    }

    @Override
    public List<Menu> getMenusTree() {
        List<Menu> allMenus = menuRepository.findAllOrderBySort();
        return buildTree(allMenus, 0L);
    }

    private List<Menu> buildTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(buildChildren(menus, menu.getMenuId())))
                .collect(Collectors.toList());
    }

    private List<Menu> buildChildren(List<Menu> menus, Long parentId) {
        List<Menu> children = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(buildChildren(menus, menu.getMenuId())))
                .collect(Collectors.toList());
        return children.isEmpty() ? null : children;
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu createMenu(Menu menu) {
        if (menuRepository.existsByMenuName(menu.getMenuName())) {
            throw new RuntimeException("菜单名称已存在");
        }
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        if (menuRepository.existsByMenuNameAndMenuIdNot(menu.getMenuName(), menu.getMenuId())) {
            throw new RuntimeException("菜单名称已存在");
        }
        Menu existing = menuRepository.findById(menu.getMenuId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("菜单不存在");
        }
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        long childrenCount = menuRepository.findByParentIdAndDelFlag(id, "0").size();
        if (childrenCount > 0) {
            throw new RuntimeException("请先删除子菜单");
        }
        menu.setDelFlag("1");
        menuRepository.save(menu);
    }

    @Override
    public void initMenus() {
        if (menuRepository.existsByMenuName("系统管理")) {
            return;
        }
        
        Menu systemMenu = new Menu();
        systemMenu.setParentId(0L);
        systemMenu.setMenuName("系统管理");
        systemMenu.setPath("/system");
        systemMenu.setComponent("Layout");
        systemMenu.setIcon("Setting");
        systemMenu.setMenuType("M");
        systemMenu.setSortNum(1);
        systemMenu.setStatus("0");
        systemMenu = menuRepository.save(systemMenu);

        Menu userMenu = new Menu();
        userMenu.setParentId(systemMenu.getMenuId());
        userMenu.setMenuName("用户管理");
        userMenu.setPath("/system/users");
        userMenu.setComponent("system/users/index");
        userMenu.setMenuType("C");
        userMenu.setSortNum(1);
        userMenu.setStatus("0");
        menuRepository.save(userMenu);

        Menu roleMenu = new Menu();
        roleMenu.setParentId(systemMenu.getMenuId());
        roleMenu.setMenuName("角色管理");
        roleMenu.setPath("/system/roles");
        roleMenu.setComponent("system/roles/index");
        roleMenu.setMenuType("C");
        roleMenu.setSortNum(2);
        roleMenu.setStatus("0");
        menuRepository.save(roleMenu);

        Menu menuMenu = new Menu();
        menuMenu.setParentId(systemMenu.getMenuId());
        menuMenu.setMenuName("菜单管理");
        menuMenu.setPath("/system/menus");
        menuMenu.setComponent("system/menus/index");
        menuMenu.setMenuType("C");
        menuMenu.setSortNum(3);
        menuMenu.setStatus("0");
        menuRepository.save(menuMenu);

        Menu deptMenu = new Menu();
        deptMenu.setParentId(systemMenu.getMenuId());
        deptMenu.setMenuName("部门管理");
        deptMenu.setPath("/system/depts");
        deptMenu.setComponent("system/depts/index");
        deptMenu.setMenuType("C");
        deptMenu.setSortNum(4);
        deptMenu.setStatus("0");
        menuRepository.save(deptMenu);
    }
}
