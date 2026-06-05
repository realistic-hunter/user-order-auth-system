# User Order Auth System

Spring Boot learning project for user login, JWT authentication, RBAC permission checks, and order management.

## Tech Stack

- Java 25
- Spring Boot 3.5.13
- MyBatis
- MySQL
- Lombok
- Spring Validation
- Spring AOP
- Redis / Spring Data Redis
- Springdoc OpenAPI / Swagger UI

## Project Highlights

- Stateless login authentication based on JWT.
- Password encryption and verification based on BCrypt.
- Request authentication through a Spring MVC `HandlerInterceptor`.
- Current user context isolation through `ThreadLocal`.
- RBAC permission model: user -> role -> permission.
- Declarative permission checks through custom annotation `@RequirePermission` and Spring AOP.
- Redis Cache Aside for user permission codes, with a 30-minute TTL and database fallback.
- Redis login failure counters: 5 failed attempts trigger a 15-minute login restriction.
- JWT logout blacklist stored by SHA-256 token digest, with TTL equal to the token's remaining lifetime.
- Unified API response and global exception handling.
- Reproducible MySQL initialization scripts in `src/main/resources/sql`.

## Main Features

- User login with JWT token.
- Unified API response wrapper: `Result<T>`.
- Global exception handling for business errors, validation errors, and unknown errors.
- Permission validation through custom annotation `@RequirePermission` and AOP.
- User query APIs.
- Role permission query API.
- Order creation, query, status update, deletion, and paginated admin order query.

## Order APIs

| Method | Path | Description | Permission |
| --- | --- | --- | --- |
| `POST` | `/orders` | Create an order for the current logged-in user | Login required |
| `GET` | `/orders/my` | Query current user's orders | Login required |
| `GET` | `/orders` | Query all orders | `order:list` |
| `GET` | `/orders/page` | Paginated admin order query, supports optional status filter | `order:list` |
| `PUT` | `/orders/{id}/status` | Update order status | `order:update` |
| `DELETE` | `/orders/{id}` | Delete order | `order:delete` |

## Redis Design

| Scenario | Redis key | TTL | Failure strategy |
| --- | --- | --- | --- |
| Permission cache | `auth:permission:user:{userId}` | 30 minutes | Fall back to MySQL |
| Login failure counter | `auth:login:failure:{username}` | 15 minutes | Allow login flow to continue |
| Logged-out JWT blacklist | `auth:token:blacklist:{sha256(token)}` | JWT remaining lifetime | Propagate the error to preserve the security boundary |

Permission checks use the Cache Aside pattern:

1. Read permission codes from Redis.
2. On a cache miss, query MySQL.
3. Write the result, including an empty permission list, back to Redis.
4. Use TTL as the current consistency guarantee. When permission mutation APIs are added, they should evict the affected user's cache after the database transaction commits.

Start a local Redis instance with Docker:

```powershell
docker run --name user-order-redis -p 6379:6379 -d redis:7-alpine
```

Create the local configuration from the tracked template:

```powershell
Copy-Item src/main/resources/application.example.yml src/main/resources/application.yml
```

Redis connection settings can be overridden with:

```text
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD
```

Logout endpoint:

```http
POST /auth/logout
Authorization: Bearer <token>
```

After logout, the same JWT is rejected even if its signature and expiration time are still valid.

## Paginated Order Query

Endpoint:

```http
GET /orders/page?pageNum=1&pageSize=10&status=1
Authorization: Bearer <admin-token>
```

Request parameters:

| Parameter | Required | Default | Description |
| --- | --- | --- | --- |
| `pageNum` | No | `1` | Current page number. Values less than 1 fall back to 1. |
| `pageSize` | No | `10` | Page size. Values less than 1 fall back to 10. |
| `status` | No | None | Optional order status filter. |

Response type:

```java
Result<PageVO<OrderVO>>
```

Example response:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 21,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 3,
    "records": [
      {
        "id": 21,
        "orderNo": "ORD1717214400000",
        "userId": 1,
        "amount": 99.90,
        "status": 1,
        "remark": "测试订单",
        "createTime": "2024-06-01T12:00:00",
        "updateTime": "2024-06-01T12:00:00"
      }
    ]
  }
}
```

Implementation notes:

- Pagination uses MyBatis SQL with `limit #{pageSize} offset #{offset}`.
- No PageHelper dependency is introduced.
- `OrderService` calculates `offset` and `pages`.
- `OrderMapper` provides separate methods for paginated records and total count.
- SQL parameters use MyBatis `#{}` binding to avoid string-concatenation injection risks.

## HTTP Client Tests

IntelliJ HTTP Client test cases are in:

```text
test-order.http
```

The file includes examples for:

- First page with 10 records.
- `pageNum=2&pageSize=5`.
- Filtering by `status`.
- Admin authorization placeholder: `Authorization: Bearer <admin-token>`.

## Database Setup

The SQL initialization scripts are in:

```text
src/main/resources/sql
```

Run them in this order:

```powershell
mysql -uroot -p < src/main/resources/sql/schema.sql
mysql -uroot -p < src/main/resources/sql/data.sql
mysql -uroot -p < src/main/resources/sql/sample-data.sql
```

Default test accounts:

| Username | Password | Role |
| --- | --- | --- |
| `lisi` | `123456` | Admin |
| `zhangsan` | `123456` | Customer |

## API Documentation

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

The paginated order endpoint and VO fields are documented with OpenAPI annotations.

## Run

Configure MySQL in `src/main/resources/application.yml`, initialize the database, then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Recent Commit Scope

- Added generic `PageVO<T>` for paginated responses.
- Added `OrderVO` for order list responses.
- Added `GET /orders/page` admin pagination API.
- Added MyBatis `limit/offset` pagination and total-count query.
- Added IntelliJ HTTP Client test cases for the pagination endpoint.
