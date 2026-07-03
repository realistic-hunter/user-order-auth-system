DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id)
);

CREATE TABLE role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (role_id, permission_id)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    remark VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (id, username, password, nickname, status) VALUES
(1, 'lisi', '$2a$10$lOtOm1nzhkGYqmiFJnYWoe/0J0pcL9EGrJXv4v5a/subFTTesBilu', '李四', 1),
(2, 'zhangsan', '$2a$10$lOtOm1nzhkGYqmiFJnYWoe/0J0pcL9EGrJXv4v5a/subFTTesBilu', '张三', 1);

INSERT INTO roles (id, role_name, role_code, description) VALUES
(1, '普通用户', 'CUSTOMER', '普通下单用户'),
(2, '管理员', 'ADMIN', '系统管理员');

INSERT INTO permissions (id, permission_name, permission_code, description) VALUES
(1, '创建订单', 'order:create', '允许创建订单'),
(2, '查看订单', 'order:view', '允许查看自己的订单'),
(3, '查看全部订单', 'order:list', '允许查看全部订单'),
(4, '修改订单状态', 'order:update', '允许修改订单状态'),
(5, '删除订单', 'order:delete', '允许删除订单');

INSERT INTO user_role (id, user_id, role_id) VALUES
(1, 1, 2),
(2, 2, 1);

INSERT INTO role_permission (id, role_id, permission_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 2, 2),
(5, 2, 3),
(6, 2, 4),
(7, 2, 5);
