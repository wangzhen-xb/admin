DELETE FROM sys_role;
DELETE FROM sys_dept;
DELETE FROM sys_user;
DELETE FROM sys_menu;
DELETE FROM sys_announcement;

INSERT INTO sys_role (role_name, role_key, role_sort, status, remark) VALUES ('管理员', 'admin', 1, '0', '系统管理员');
INSERT INTO sys_role (role_name, role_key, role_sort, status, remark) VALUES ('普通用户', 'user', 2, '0', '普通用户权限');

INSERT INTO sys_dept (dept_id, parent_id, dept_name, sort_num, status, leader, phone, email) VALUES (1, 0, '总部', 1, '0', '管理员', '13800138000', 'admin@example.com');

INSERT INTO sys_user (username, password, email, phone, dept_id, status, create_time) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'admin@example.com', '13800138000', 1, '0', '2024-01-01 00:00:00');
INSERT INTO sys_user (username, password, email, phone, dept_id, status, create_time) VALUES ('user', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'user@example.com', '13800138001', 1, '0', '2024-01-02 00:00:00');
INSERT INTO sys_user (username, password, email, phone, dept_id, status, create_time) VALUES ('test', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'test@example.com', '13800138002', 1, '0', '2024-01-03 00:00:00');

INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, icon, menu_type, sort_num, status) VALUES (1, 0, '系统管理', '/system', 'Layout', 'Setting', 'M', 1, '0');
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, icon, menu_type, sort_num, status) VALUES (2, 1, '用户管理', '/system/users', 'system/users/index', 'User', 'C', 1, '0');
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, icon, menu_type, sort_num, status) VALUES (3, 1, '角色管理', '/system/roles', 'system/roles/index', 'User', 'C', 2, '0');
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, icon, menu_type, sort_num, status) VALUES (4, 1, '菜单管理', '/system/menus', 'system/menus/index', 'Menu', 'C', 3, '0');
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, component, icon, menu_type, sort_num, status) VALUES (5, 1, '部门管理', '/system/depts', 'system/depts/index', 'Building', 'C', 4, '0');

INSERT INTO sys_announcement (title, content, type, status, publish_time, create_by) VALUES ('系统升级通知', '系统将于今晚22:00-24:00进行维护升级，请提前保存工作内容。', 'notice', '0', '2024-01-15 10:00:00', 'admin');
INSERT INTO sys_announcement (title, content, type, status, publish_time, create_by) VALUES ('安全提醒', '请定期更换密码，建议每90天更换一次，确保账户安全。', 'alert', '0', '2024-01-14 15:30:00', 'admin');
INSERT INTO sys_announcement (title, content, type, status, publish_time, create_by) VALUES ('功能更新', '新增数据导出功能，支持Excel和CSV格式，欢迎使用！', 'info', '0', '2024-01-13 09:00:00', 'admin');
INSERT INTO sys_announcement (title, content, type, status, publish_time, create_by) VALUES ('假期安排', '春节假期安排：1月29日至2月6日放假，2月7日正常上班。', 'notice', '0', '2024-01-12 14:00:00', 'admin');
