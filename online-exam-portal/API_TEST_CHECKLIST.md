# Online Exam Portal API Test Checklist

Base URL: `http://localhost:9090`

Use header for protected APIs:

```http
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

## 1) Auth APIs (`/api/auth`) - Public

| Method | Endpoint | Auth | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | No | Register user |
| POST | `/api/auth/login` | No | Login and get JWT token |

### Sample request bodies

`POST /api/auth/register`

```json
{
  "username": "student1",
  "email": "student1@example.com",
  "password": "Password@123",
  "role": "STUDENT"
}
```

`POST /api/auth/login`

```json
{
  "email": "student1@example.com",
  "password": "Password@123"
}
```

---

## 2) User APIs (`/api/users`)

| Method | Endpoint | Auth/Role | Purpose |
|---|---|---|---|
| GET | `/api/users/profile` | Any authenticated user | Get own profile |
| PUT | `/api/users/update-profile` | Any authenticated user | Update own profile (username) |
| POST | `/api/users/change-password` | Any authenticated user | Change own password |
| GET | `/api/users/all` | ADMIN only | Get all users |
| GET | `/api/users/{id}` | ADMIN or TEACHER | Get user by id (TEACHER limited in controller) |
| GET | `/api/users/by-id/{id}` | ADMIN or TEACHER | Legacy route of get-by-id |
| PUT | `/api/users/{id}` | Any authenticated user (controller enforces admin-or-self) | Update user |
| DELETE | `/api/users/{id}` | ADMIN only | Delete user |

### Controller rule for `PUT /api/users/{id}`

- ADMIN: can update any user.
- Non-admin: can update only own profile (`id` must match logged-in user id).
- Non-admin cannot change role.

### Sample request bodies

`PUT /api/users/update-profile`

```json
{
  "username": "new-name"
}
```

`POST /api/users/change-password`

```json
{
  "oldPassword": "Password@123",
  "newPassword": "Password@456"
}
```

`PUT /api/users/{id}`

```json
{
  "username": "updated-user",
  "email": "updated@example.com",
  "role": "TEACHER"
}
```

---

## 3) Question APIs (`/api/questions`) 

> Security config currently requires `ADMIN` or `TEACHER` for all `/api/questions/**` routes.

| Method | Endpoint | Auth/Role | Purpose |
|---|---|---|---|
| POST | `/api/questions/upload` | ADMIN or TEACHER | Create question with options |
| GET | `/api/questions/subject/{subjectId}` | ADMIN or TEACHER | List questions by subject |
| GET | `/api/questions/subject/{subjectId}/question/{questionId}` | ADMIN or TEACHER | Get one question |
| PUT | `/api/questions/subject/{subjectId}/question/{questionId}` | ADMIN or TEACHER | Update question |
| DELETE | `/api/questions/subject/{subjectId}/question/{questionId}` | ADMIN or TEACHER | Delete question |

---

## 4) Option APIs (`/api/options`)

> No specific `/api/options/**` matcher in `SecurityConfig`; these fall under `.anyRequest().authenticated()`.

| Method | Endpoint | Auth/Role | Purpose |
|---|---|---|---|
| POST | `/api/options/subject/{subjectId}/question/{questionId}` | Any authenticated user | Add option |
| GET | `/api/options/subject/{subjectId}/question/{questionId}` | Any authenticated user | Get options |
| PUT | `/api/options/subject/{subjectId}/question/{questionId}/option/{optionId}` | Any authenticated user | Update one option |
| PUT | `/api/options/subject/{subjectId}/question/{questionId}` | Any authenticated user | Update all options |
| DELETE | `/api/options/subject/{subjectId}/question/{questionId}/option/{optionId}` | Any authenticated user | Delete option |

---

## 5) Management APIs (`/api/management`)

> No explicit `/api/management/**` role matcher in current `SecurityConfig`; these also fall under `.anyRequest().authenticated()`.

| Method | Endpoint | Auth/Role | Purpose |
|---|---|---|---|
| POST | `/api/management/subjects` | Any authenticated user | Create subject |
| DELETE | `/api/management/subjects/{id}` | Any authenticated user | Delete subject |
| POST | `/api/management/exams/{subjectId}` | Any authenticated user | Create exam |
| GET | `/api/management/exams` | Any authenticated user | List exams |

---

## 6) Endpoint in Security But Not Found in Current Controllers

| Method | Endpoint | Security Rule | Note |
|---|---|---|---|
| POST | `/api/exams/attempt` | STUDENT only | No matching controller method found currently |

---

## 7) Quick Test Flow

1. Register users for ADMIN, TEACHER, STUDENT.
2. Login each and save JWT token.
3. Verify role-based access with each endpoint above.
4. Specifically verify:
   - Non-admin cannot `PUT /api/users/{otherUserId}`.
   - Logged-in user can `PUT /api/users/{selfId}`.
   - Non-admin cannot change role in `PUT /api/users/{selfId}`.

---

## 8) cURL Templates

```bash
curl -X POST "http://localhost:9090/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"student1@example.com","password":"Password@123"}'
```

```bash
curl -X GET "http://localhost:9090/api/users/profile" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

```bash
curl -X PUT "http://localhost:9090/api/users/5" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"username":"new-name"}'
```

