# Vinylo Backend

The Spring Boot API for **Vinylo**, a year 2, semester 3 project which was a web platform for vinyl collectors. Users can manage a record collection, list records for sale, bid on other users' listings in real time, chat in a public room, and listen to album previews through Spotify.

The front-end is in [vinylo-individual-front-end](../vinylo-individual-front-end). Design and planning documents are in [vinylo-individual-documentation](../vinylo-individual-documentation).

## Features

- **Accounts and auth:** registration, login, and JWT access tokens (valid for 30 minutes) with a refresh endpoint
- **Roles:** regular users and premium users. Users can upgrade their profile
- **Collections:** add records to a personal collection and remove them again
- **Vinyl catalogue:** records linked to artists, with type, speed, colour, state, price and stock
- **Auctions and bids:** create, edit and delete auctions; place bids on running auctions
- **Real-time updates:** WebSocket (STOMP) topics for auctions, collections, artist and profile changes, and a public chat
- **Spotify:** token handling and an embed endpoint so the front-end can show album previews

## Tech stack

- Java 17
- Spring Boot 3.3.3 (Web, Data JPA, Validation, WebSocket, Security)
- JJWT 0.11.5 for tokens
- MySQL 8 with Flyway migrations
- JUnit 5, Mockito, JaCoCo, SonarQube plugin
- Gradle (wrapper included)

## Project layout

The Gradle project lives in `Vinylo-Individual/`.

```
Vinylo-Individual/src/main/java/s3/individual/vinylo/
├── controllers/      # REST endpoints (auctions, bids, collections, profiles, users, vinyls, login, Spotify, ...)
├── services/         # Service interfaces
├── serviceimpl/      # Service implementations
├── persistence/      # JPA repositories and entities
├── domain/           # Domain models and DTOs
├── configuration/    # WebSocket, CORS, security and JWT configuration
└── exceptions/       # Custom exceptions and a global handler
src/main/resources/
├── application.properties
└── db/migration/
src/test/java/...     # Unit, repository and integration tests
```

## API overview

Base URL: `http://<host>:8080`

| Area | Routes |
|---|---|
| Auth | `POST /login`, `POST /refresh-token` |
| Users | `GET /users`, `GET /users/{id}`, `POST /users`, `DELETE /users/{id}` |
| Profiles | `GET /profile/{id}`, `PUT /profile/{id}`, `PUT /profile/upgrade/{id}` |
| Vinyls | `GET /vinyls`, `GET /vinyls/{id}`, `POST /vinyls`, `PUT /vinyls/{id}`, `DELETE /vinyls/{id}`, `GET /vinyls/enums` |
| Collections | `GET /collections`, `POST /collections`, `GET /collections/user/{userId}`, `GET /collections/{id}`, `DELETE /collections/{vinylId}/{userId}` |
| Artists | `GET /artist`, `GET /artist/{id}`, `POST /artist`, `PUT /artist/{id}`, `DELETE /artist/{artistId}` |
| Auctions | `GET /auctions`, `GET /auctions/{id}`, `POST /auctions`, `PUT /auctions/{id}`, `DELETE /auctions/{id}` |
| Bids | `POST /bids/{auctionId}` |
| Spotify | `GET /spotify-embed` |
| Notifications | `POST /notifications` (broadcasts to `/topic/publicmessages`) |

Routes marked as public in the security configuration (login, registration, and several read routes) need no token. All other routes need a bearer token in the `Authorization` header.

WebSocket: connect to `ws://<host>:8080/ws` using STOMP. Topics include `/topic/publicmessages`, `/topic/auction/`, `/topic/artists/`, `/topic/collections/`, `/topic/profileUpdated` and `/topic/ArtistUpdated`. User-specific messages go to `/user/{userId}`.

## Requirements

- JDK 17
- MySQL 8.x
- A Spotify developer app (client ID and secret) if you want the Spotify features to work
- Docker (optional)

## Configuration

The app needs these properties. Set them as environment variables so no secrets end up in the repository. Spring maps environment variables to properties automatically (for example `JWT_SECRET` sets `jwt.secret`):

| Property | Environment variable | Purpose |
|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | JDBC URL, for example `jdbc:mysql://localhost:3306/mysql-db-vinylo` |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | Database user |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | Database password |
| `jwt.secret` | `JWT_SECRET` | Secret used to sign tokens. Use a long random string |
| `spotify.api.token_url` | `SPOTIFY_API_TOKEN_URL` | Spotify token endpoint |
| `spotify.api.client_id` | `SPOTIFY_API_CLIENT_ID` | Spotify client ID |
| `spotify.api.client_secret` | `SPOTIFY_API_CLIENT_SECRET` | Spotify client secret |

## Database setup

1. Create the database:

   ```sql
   CREATE DATABASE `mysql-db-vinylo`;
   ```

2. Create a dedicated user with rights on that database.
3. Flyway applies the migrations in `src/main/resources/db/migration` on startup.

## Running locally

From `Vinylo-Individual/`:

```bash
chmod +x gradlew      # once, on macOS/Linux
./gradlew bootRun     # Windows: gradlew.bat bootRun
```

The API runs on port 8080 by default.

## Running with Docker

A Dockerfile is in `Vinylo-Individual/`. The docker-compose setup lives in the front-end repository, because it starts the database, the backend and the frontend together. See that README.

## Tests

```bash
cd Vinylo-Individual
./gradlew test
```

The suite contains controller tests, service tests with Mockito, JPA repository tests, and integration tests. Test settings are in `src/test/resources/application-test.properties`, and seed data is in `src/test/resources/data.sql`.

Coverage (JaCoCo) and static analysis (SonarQube) are configured in `build.gradle`:

```bash
./gradlew jacocoTestReport
./gradlew sonar   # needs a SonarQube server and token
```

## Continuous integration

`.gitlab-ci.yml` runs build, test, Sonar analysis (allowed to fail) and a Docker image build.

## Known limitations

- **CORS origins are hardcoded** in `configuration/WebConfig.java` as `localhost:5173` and `localhost:3000`. The `CORS_ALLOWED_ORIGINS` variable in the front-end's `docker-compose.yml` is not read by the code, so adding a new origin means editing that file.
- The `README.md` in the repository root is the default GitLab Spring template. This file replaces it.
- `spring.jpa.show-sql=true` logs every SQL statement. Turn it off outside development.

## Related repositories

- [vinylo-individual-front-end](../vinylo-individual-front-end): the React web app
- [vinylo-individual-documentation](../vinylo-individual-documentation): project plan, design document, diagrams and reports

## Author

Szymon Gancarz. Built as an individual project in the ICT programme at Fontys University of Applied Sciences.
