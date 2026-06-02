USE user_order_auth;

INSERT INTO users (id, username, password, nickname, status, create_time, update_time) VALUES
(1, 'lisi', '$2a$10$lOtOm1nzhkGYqmiFJnYWoe/0J0pcL9EGrJXv4v5a/subFTTesBilu', '李四', 1, '2026-04-06 16:11:01', '2026-05-18 14:21:45'),
(2, 'zhangsan', '$2a$10$lOtOm1nzhkGYqmiFJnYWoe/0J0pcL9EGrJXv4v5a/subFTTesBilu', '张三', 1, '2026-05-08 13:47:02', '2026-05-18 14:22:05');

INSERT INTO roles (id, role_name, role_code, description, create_time, update_time) VALUES
(1, '普通用户', 'CUSTOMER', '普通下单用户', '2026-04-06 20:37:34', '2026-04-06 20:37:34'),
(2, '管理员', 'ADMIN', '系统管理员', '2026-04-06 20:37:38', '2026-04-06 20:37:38');

INSERT INTO permissions (id, permission_name, permission_code, description, create_time, update_time) VALUES
(1, '创建订单', 'order:create', '允许创建订单', '2026-04-06 21:15:05', '2026-04-06 21:15:05'),
(2, '查看订单', 'order:view', '允许查看自己的订单', '2026-04-06 21:15:03', '2026-04-06 21:15:03'),
(3, '查看全部订单', 'order:list', '允许查看全部订单', '2026-04-18 19:41:01', '2026-04-18 19:41:01'),
(4, '修改订单状态', 'order:update', '允许修改订单状态', '2026-04-15 22:53:57', '2026-04-15 22:53:57'),
(5, '删除订单', 'order:delete', '允许删除订单', '2026-04-18 19:40:53', '2026-04-18 19:40:53'),
(6, '新增用户', 'user:add', '允许新增系统用户', '2026-04-06 21:15:12', '2026-04-06 21:15:12'),
(7, '删除用户', 'user:delete', '允许删除系统用户', '2026-04-06 21:15:08', '2026-04-06 21:15:08');

INSERT INTO user_role (id, user_id, role_id, create_time) VALUES
(1, 1, 2, '2026-04-18 20:15:17'),
(2, 2, 1, '2026-05-08 13:47:46');

INSERT INTO role_permission (id, role_id, permission_id, create_time) VALUES
(1, 1, 1, '2026-04-06 21:33:12'),
(2, 1, 2, '2026-04-06 21:33:08'),
(3, 2, 1, '2026-04-06 21:33:23'),
(4, 2, 2, '2026-04-06 21:33:08'),
(5, 2, 3, '2026-04-18 20:13:41'),
(6, 2, 4, '2026-04-18 20:13:35'),
(7, 2, 5, '2026-04-18 20:13:39'),
(8, 2, 6, '2026-04-06 21:33:25'),
(9, 2, 7, '2026-04-06 21:33:25');
