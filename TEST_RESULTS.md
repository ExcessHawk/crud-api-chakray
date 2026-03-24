# Test Results - crud-api-chakray

Servidor: `http://localhost:8080`  
Spring Boot 4.0.4 / Java 17

---

## GET /users

Lista todos los usuarios sin filtros.

**Request**
```
GET /users
```

**Response 200**
```json
[
  {
    "id": "45208807-c240-402b-b6a3-0e01ae38a073",
    "email": "user1@mail.com",
    "name": "user1",
    "phone": "+1 55 555 555 55",
    "tax_id": "AARR990101XXX",
    "created_at": "01-01-2026 00:00",
    "addresses": [
      { "id": 1, "name": "workaddress", "street": "street No. 1", "country_code": "UK" },
      { "id": 2, "name": "homeaddress", "street": "street No. 2", "country_code": "AU" }
    ]
  },
  {
    "id": "1eb3629b-e788-4147-841f-993ba5003a5c",
    "email": "user2@mail.com",
    "name": "user2",
    "phone": "+52 55 5555 5555",
    "tax_id": "BOBB850101XXX",
    "created_at": "01-02-2026 12:30",
    "addresses": [
      { "id": 1, "name": "workaddress", "street": "street No. 3", "country_code": "MX" },
      { "id": 2, "name": "homeaddress", "street": "street No. 4", "country_code": "US" }
    ]
  },
  {
    "id": "ae95dd95-c510-4748-b34b-463636e9a111",
    "email": "user3@mail.com",
    "name": "user3",
    "phone": "+1 555 555 1234",
    "tax_id": "CAPC900101XXX",
    "created_at": "01-03-2026 08:15",
    "addresses": [
      { "id": 1, "name": "workaddress", "street": "street No. 5", "country_code": "DE" },
      { "id": 2, "name": "homeaddress", "street": "street No. 6", "country_code": "FR" }
    ]
  }
]
```

---

## GET /users?sortedBy=email

**Request**
```
GET /users?sortedBy=email
```

**Response 200** — usuarios ordenados alfabéticamente por email (user1, user2, user3). ✅

---

## GET /users?filter=name+co+user

Nota: el `+` debe enviarse como `%2B` en la URL.

**Request**
```
GET /users?filter=name%2Bco%2Buser
```

**Response 200** — devuelve los 3 usuarios (todos contienen "user" en el nombre). ✅

---

## GET /users?filter=email+ew+mail.com

**Request**
```
GET /users?filter=email%2Bew%2Bmail.com
```

**Response 200** — devuelve los 3 usuarios (todos terminan en "mail.com"). ✅

---

## GET /users?filter=tax_id+eq+AARR990101XXX

**Request**
```
GET /users?filter=tax_id%2Beq%2BAARR990101XXX
```

**Response 200**
```json
[
  {
    "id": "45208807-c240-402b-b6a3-0e01ae38a073",
    "email": "user1@mail.com",
    "name": "user1",
    "tax_id": "AARR990101XXX",
    "created_at": "01-01-2026 00:00",
    ...
  }
]
```
Devuelve exactamente 1 usuario. ✅

---

## POST /users — crear usuario

**Request**
```
POST /users
Content-Type: application/json
```
```json
{
  "email": "newuser@mail.com",
  "name": "newuser",
  "phone": "+52 55 1234 5678",
  "password": "secret123",
  "tax_id": "XAXX010101000",
  "addresses": [
    { "id": 1, "name": "home", "street": "Calle 1", "country_code": "MX" }
  ]
}
```

**Response 201**
```json
{
  "id": "2aece0fc-0fae-4ce3-b850-3c41f6689140",
  "email": "newuser@mail.com",
  "name": "newuser",
  "phone": "+52 55 1234 5678",
  "tax_id": "XAXX010101000",
  "created_at": "24-03-2026 07:59",
  "addresses": [
    { "id": 1, "name": "home", "street": "Calle 1", "country_code": "MX" }
  ]
}
```
Password no aparece en la respuesta. ✅

---

## POST /users — RFC inválido

**Request**
```json
{
  "email": "bad@mail.com",
  "name": "test",
  "phone": "+52 55 1234 5678",
  "password": "pass",
  "tax_id": "INVALID",
  "addresses": [{ "id": 1, "name": "h", "street": "s", "country_code": "MX" }]
}
```

**Response 400**
```json
{ "error": "taxId: Invalid Mexican RFC format" }
```
✅

---

## POST /users — tax_id duplicado

**Request** — mismo `tax_id` que user1 (`AARR990101XXX`).

**Response 409**
```json
{ "error": "tax_id is already registered" }
```
✅

---

## PATCH /users/{id} — actualizar usuario

**Request**
```
PATCH /users/2aece0fc-0fae-4ce3-b850-3c41f6689140
Content-Type: application/json
```
```json
{ "name": "newuser-updated" }
```

**Response 200**
```json
{
  "id": "2aece0fc-0fae-4ce3-b850-3c41f6689140",
  "email": "newuser@mail.com",
  "name": "newuser-updated",
  "phone": "+52 55 1234 5678",
  "tax_id": "XAXX010101000",
  "created_at": "24-03-2026 07:59",
  "addresses": [
    { "id": 1, "name": "home", "street": "Calle 1", "country_code": "MX" }
  ]
}
```
✅

---

## DELETE /users/{id} — eliminar usuario

**Request**
```
DELETE /users/2aece0fc-0fae-4ce3-b850-3c41f6689140
```

**Response 204** — sin body. ✅

---

## DELETE /users/{id} — usuario no existe

**Request**
```
DELETE /users/00000000-0000-0000-0000-000000000000
```

**Response 404**
```json
{ "error": "User not found: 00000000-0000-0000-0000-000000000000" }
```
✅

---

## POST /login — credenciales correctas

**Request**
```
POST /login
Content-Type: application/json
```
```json
{ "tax_id": "AARR990101XXX", "password": "demo-password" }
```

**Response 200**
```json
{
  "id": "45208807-c240-402b-b6a3-0e01ae38a073",
  "message": "Authenticated"
}
```
✅

---

## POST /login — password incorrecto

**Request**
```json
{ "tax_id": "AARR990101XXX", "password": "wrongpass" }
```

**Response 401**
```json
{ "error": "Invalid credentials" }
```
✅

---

## Resumen

| Endpoint | Escenario | Status | Resultado |
|---|---|---|---|
| `GET /users` | lista todos | 200 | ✅ |
| `GET /users?sortedBy=email` | ordenado por email | 200 | ✅ |
| `GET /users?filter=name+co+user` | contiene "user" | 200 | ✅ |
| `GET /users?filter=email+ew+mail.com` | termina en "mail.com" | 200 | ✅ |
| `GET /users?filter=tax_id+eq+AARR990101XXX` | igual a RFC | 200 | ✅ |
| `POST /users` | crear usuario válido | 201 | ✅ |
| `POST /users` | RFC inválido | 400 | ✅ |
| `POST /users` | tax_id duplicado | 409 | ✅ |
| `PATCH /users/{id}` | actualizar nombre | 200 | ✅ |
| `DELETE /users/{id}` | eliminar existente | 204 | ✅ |
| `DELETE /users/{id}` | no existe | 404 | ✅ |
| `POST /login` | credenciales correctas | 200 | ✅ |
| `POST /login` | password incorrecto | 401 | ✅ |
