# Task4 Module6 Server

Sample credentials for `POST /auth/login`:

- `login`: `admin`
- `password`: `admin`

JWT token lifetime: 30 minutes.

Main endpoints:

- `POST /auth/login`
- `GET /prizes` (JWT token required)
- `GET /prizes/{year}/{category}` (JWT token required)
- `GET /prizes/{year}/{category}/laureates` (JWT token required)
