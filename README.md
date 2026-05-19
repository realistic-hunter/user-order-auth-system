# 1. 项目简介

## 用户-订单-权限系统
一个基于 Spring Boot + MyBatis + JWT + RBAC 的后台管理系统，实现了用户管理、订单管理、登录认证与权限控制等功能。

本项目主要用于练习：
- Spring Boot 后端开发
- RESTful API 设计
- JWT 登录认证
- RBAC 权限模型
- MyBatis 数据持久化
- Java 后端三层架构

登录模块使用 JWT 实现无状态认证，用户登录成功后返回 token，后续请求通过拦截器解析 token 获取当前用户信息。密码存储方面，使用 BCrypt 对用户密码进行加密保存，登录时通过 BCrypt 的 matches 方法校验明文密码与数据库密文，避免数据库中存储明文密码。

2. 项目功能

## 用户模块
- 用户注册
- 用户登录
- 用户信息查询
- 用户列表查询

## 订单模块
- 创建订单
- 查询用户订单
- 修改订单状态
- 查询全部订单（管理员）
- 删除订单（管理员）

## 权限模块
采用 RBAC（Role-Based Access Control）权限模型：
- 用户（User）
- 角色（Role）
- 权限（Permission）
实现：
- 用户与角色多对多
- 角色与权限多对多
- 基于 JWT 的登录认证
- 基于角色的接口权限控制

3. 技术栈

## 技术栈
| 技术 | 说明 |
|---|---|
| Spring Boot 3 | 后端框架 |
| MyBatis | ORM 持久层框架 |
| MySQL | 数据库 |
| JWT | 登录认证 |
| Lombok | 简化 Java 代码 |
| Maven | 项目管理 |
| IntelliJ IDEA | 开发工具 |

4. 项目结构

## 项目结构

src/main/java/com/liushipin/userorderauthsystem

├── controller    # 接口层   
├── service       # 业务层   
├── mapper        # 数据访问层   
├── entity        # 实体类   
├── dto           # 数据传输对象   
├── vo            # 返回对象   
├── config        # 配置类   
├── interceptor   # JWT 拦截器   
├── exception     # 全局异常处理   
└── util          # 工具类   

5. 数据库设计

## 数据库设计
项目共包含 6 张核心数据表：
- users：用户表
- roles：角色表
- permissions：权限表
- user_role：用户角色关联表
- role_permission：角色权限关联表
- orders：订单表

6. 接口展示

## 接口展示
### 用户登录
POST /login
请求参数：
{
  "username": "admin",
  "password": "123456"
}

返回结果：
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "xxxxxx"
  }
}

7. 权限设计（重点）

## 权限控制
项目基于 RBAC 权限模型实现接口访问控制。

普通用户：
- 只能查看自己的订单

管理员：
- 可以查看所有订单
- 可以删除订单
- 可以管理用户信息

接口通过 JWT Token 进行身份认证，
并结合角色信息完成权限校验。

8. 项目运行

## 项目运行
### 1. 克隆项目
git clone xxxxxx

### 2. 配置数据库
修改 application.yml：
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_order_auth

### 3. 启动项目
运行：
UserOrderAuthSystemApplication

### 4. 接口测试
使用：
- IntelliJ HTTP Client
- Postman

9. 后续优化方向

## 后续优化方向
- Redis 缓存
- Spring Security
- Docker 部署
- 接口限流
- 日志系统
- Swagger 接口文档
- 单元测试
- 分页查询


