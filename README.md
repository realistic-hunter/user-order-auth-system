# User Order Auth System

Spring Boot learning project for user login, JWT authentication, permission checks, and order management.

## Tech Stack

- Java 25
- Spring Boot 3.5.13
- MyBatis
- MySQL
- Lombok
- Spring Validation
- Spring AOP
- Springdoc OpenAPI / Swagger UI

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
- Admin authorization placeholder: `Authorization: Bearer 管理员token`.

## API Documentation

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

The paginated order endpoint and VO fields are documented with OpenAPI annotations.

## Run

Configure MySQL in `src/main/resources/application.yml`, then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Recent Commit Scope

- Added generic `PageVO<T>` for paginated responses.
- Added `OrderVO` for order list responses.
- Added `GET /orders/page` admin pagination API.
- Added MyBatis `limit/offset` pagination and total-count query.
- Added IntelliJ HTTP Client test cases for the pagination endpoint.
