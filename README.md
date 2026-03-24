# crud-api-chakray

REST API hecha con Spring Boot 4 y Java 17 para la prueba técnica de Chakray.

Guarda usuarios en memoria, tiene sorting, filtering, encriptación de passwords con AES256, validación de RFC y teléfono (AndresFormat), Swagger y Docker.

## Como correrlo

Necesitas tener Java 17 o 21 instalado.

```bash
./mvnw spring-boot:run
```

Corre en el puerto 8080.

- Swagger: http://localhost:8080/swagger-ui.html
- OpenAPI json: http://localhost:8080/api-docs

## Usuarios de prueba

Al iniciar la app se crean 3 usuarios automáticamente. El password de todos es `demo-password`.

| tax_id | email |
|--------|-------|
| AARR990101XXX | user1@mail.com |
| BOBB850101XXX | user2@mail.com |
| CAPC900101XXX | user3@mail.com |

## Endpoints

- `GET /users` - lista todos los usuarios
- `GET /users?sortedBy=name` - ordena por el campo indicado (email, id, name, phone, tax_id, created_at)
- `GET /users?filter=name+co+user` - filtra usuarios (ver ejemplos abajo)
- `POST /users` - crea un usuario nuevo
- `PATCH /users/{id}` - actualiza campos de un usuario
- `DELETE /users/{id}` - elimina un usuario
- `POST /login` - autenticación con tax_id y password

### Ejemplos de filter

El formato es `campo+operador+valor`. Los operadores son:
- `co` = contains
- `eq` = equals
- `sw` = starts with
- `ew` = ends with

```
GET /users?filter=name+co+user
GET /users?filter=email+ew+mail.com
GET /users?filter=phone+sw+555
GET /users?filter=tax_id+eq+AARR990101XXX
```

Nota: en la URL el `+` se manda como `%2B` cuando se hace con curl.

### Login

```
POST /login
Content-Type: application/json

{
  "tax_id": "AARR990101XXX",
  "password": "demo-password"
}
```
Regresa 401 si las credenciales son incorrectas.

## Notas

- El password nunca aparece en las respuestas
- `created_at` se muestra en zona horaria de Madagascar (Indian/Antananarivo), formato `dd-MM-yyyy HH:mm`
- `tax_id` debe tener formato RFC mexicano
- El teléfono debe tener 10 dígitos nacionales (puede incluir código de país)
- `tax_id` es único

## Docker

```bash
docker build -t crud-api-chakray .
docker run -p 8080:8080 crud-api-chakray
```
