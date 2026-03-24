# crud-api-chakray — Users REST API (Spring Boot 4, Java 17+)

In-memory user list with sorting, filtering, AES-256-GCM password storage, RFC (Mexican tax id) and AndresFormat phone validation, OpenAPI/Swagger, and Docker packaging.

## Run

```bash
./mvnw spring-boot:run
```

Requires `JAVA_HOME` (JDK 17 or 21). Default port: **8080**.

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Seeded users

Three users are created at startup (random UUIDs). Shared login password: **`demo-password`**.

| tax_id (username) | email           |
|-------------------|-----------------|
| AARR990101XXX     | user1@mail.com  |
| BOBB850101XXX     | user2@mail.com  |
| CAPC900101XXX     | user3@mail.com  |

## API

- `GET /users?sortedBy=email|id|name|phone|tax_id|created_at` — optional sort
- `GET /users?filter=field+op+value` — `op` ∈ `co` (contains), `eq`, `sw` (starts with), `ew` (ends with); `filter` must not be empty when present
- `POST /users` — create user (password encrypted; never returned)
- `PATCH /users/{id}` — partial update
- `DELETE /users/{id}`
- `POST /login` — body `{ "tax_id": "...", "password": "..." }` — **401** on failure

### Filter examples

- `/users?filter=name+co+user`
- `/users?filter=email+ew+mail.com`
- `/users?filter=phone+sw+555` (compares against the **10-digit national** number)
- `/users?filter=tax_id+eq+AARR990101XXX`

## Configuration

- **AES-256 key**: `AES_SECRET_KEY_HEX` (64 hex chars = 32 bytes), or `app.security.aes-secret-key-hex` in `application.properties`
- **created_at** display: **Indian/Antananarivo** (`dd-MM-yyyy HH:mm`)

## Docker

```bash
docker build -t crud-api-chakray .
docker run -p 8080:8080 crud-api-chakray
```
