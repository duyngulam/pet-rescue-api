# Pet Rescue API

A **minimal, runnable skeleton** demonstrating Clean Architecture (Hexagonal / Ports & Adapters) with Spring Boot.
The project contains a single **Pet CRUD flow** so you can study the architecture without noise from authentication, authorization, or external service integrations.

---

## Quick Start

### Prerequisites

| Tool | Version | Required for |
|------|---------|-------------|
| **JDK** | 21 + | Local dev (without Docker) |
| **Docker** | 20 + | Docker-based dev / deployment |
| **Gradle** | (wrapper included) | — |

> Docker is required — the app connects to PostgreSQL. Run `docker-compose up -d postgres` to start the DB.

### Run

```bash
# 1. Start PostgreSQL container
docker-compose up -d postgres

# 2. Run the app
./gradlew bootRun          # Linux / macOS
.\gradlew.bat bootRun      # Windows
```

The app starts on **<http://localhost:8080>**.

### Useful URLs

| URL | Purpose |
|-----|---------|
| <http://localhost:8080/swagger-ui.html> | Interactive API docs (Swagger UI) |
| <http://localhost:8080/api-docs> | Raw OpenAPI 3.0 spec (JSON) |
| <http://localhost:8080/actuator/health> | Health check |

### Build & Test

```bash
./gradlew build       # compile + run tests
./gradlew test        # tests only
```

### Run with Docker (local Postgres)

```bash
# Start app + PostgreSQL container
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop
docker-compose down
```

This uses profile `development` with a local PostgreSQL container.

### Run with Gradle (still needs Docker Postgres)

```bash
# Start only the Postgres container
docker-compose up -d postgres

# Run the app with Gradle
./gradlew bootRun
```

---

## Architecture Overview

The project follows **Clean Architecture** organized into four top-level packages:

```
com.uit.petrescueapi
├── domain            ← Enterprise business rules (innermost circle)
├── application       ← Use-cases & ports (application business rules)
├── infrastructure    ← Frameworks, DB, config (outermost circle)
└── presentation      ← REST controllers, exception advice (delivery mechanism)
```

### Dependency rule

Dependencies point **inward only**:

```
presentation  ──→  application  ──→  domain
infrastructure ──→  application  ──→  domain
```

- `domain` depends on **nothing** (pure Java + Lombok).
- `application` depends on `domain`.
- `infrastructure` and `presentation` depend on `application` (and transitively `domain`), but **never on each other**.

### Request flow

```
HTTP Request
   │
   ▼
┌──────────────────────────────────────────────────┐
│  CorrelationIdFilter  (assigns X-Correlation-ID) │
│  LoggingFilter        (logs request/response)    │
│  SecurityFilterChain  (permit all — skeleton)    │
└──────────────────────────────────────────────────┘
   │
   ▼
PetController                      ← presentation layer
   │  calls PetInputPort.create(cmd)
   ▼
PetUseCase (implements PetInputPort) ← application layer
   │  maps command → domain entity
   │  calls PetDomainService.create(pet)
   ▼
PetDomainService                   ← domain layer
   │  applies business rules (status = AVAILABLE, UUID, timestamps)
   │  calls PetRepository.save(pet)
   ▼
PetRepositoryAdapter               ← infrastructure layer
   │  implements PetRepository (domain port)
   │  uses PetEntityMapper to convert domain ↔ JPA entity
   │  delegates to PetJpaRepository (Spring Data)
   ▼
PostgreSQL (Docker container / NeonDB)
```

---

## Package-by-Package Breakdown

### 1. Domain Layer (`domain/`)

The innermost layer — contains **business entities, value objects, repository contracts, domain services, and exceptions**. Zero framework dependencies (only Lombok for boilerplate reduction).

| File | Purpose |
|------|---------|
| `entity/Pet.java` | **Aggregate root.** Pure POJO with all pet fields (name, species, breed, age, gender, status, etc.) plus audit fields (createdAt, updatedAt, deleted). No JPA annotations — persistence is the infrastructure's concern. |
| `valueobject/PetStatus.java` | Enum: `AVAILABLE`, `ADOPTED`, `PENDING`, `FOSTERED`, `UNAVAILABLE`. |
| `valueobject/Gender.java` | Enum: `MALE`, `FEMALE`, `UNKNOWN`. |
| `valueobject/HealthStatus.java` | Enum: `HEALTHY`, `UNDER_TREATMENT`, `RECOVERING`, `SPECIAL_NEEDS`. |
| `repository/PetRepository.java` | **Domain port** (interface). Declares `save`, `findById`, `findAll`, `findByStatus`, `deleteById`, `existsById`. Implemented by the infrastructure layer. |
| `service/PetDomainService.java` | **Business rules live here.** New pets always start as `AVAILABLE`. Status transitions are validated against an explicit allow-list (`ALLOWED_TRANSITIONS` map). Deletion is always soft-delete. `@Transactional` is applied at this layer — nowhere else. |
| `exception/BaseException.java` | Abstract base with `HttpStatus` and `errorCode`. |
| `exception/ResourceNotFoundException.java` | Thrown when a pet is not found (HTTP 404). |
| `exception/BusinessException.java` | Thrown for business rule violations (HTTP 400). |

**Key business rule — Status transition matrix:**

| From | Allowed transitions |
|------|-------------------|
| AVAILABLE | PENDING, FOSTERED, UNAVAILABLE |
| PENDING | ADOPTED, AVAILABLE |
| ADOPTED | AVAILABLE |
| FOSTERED | AVAILABLE, PENDING, ADOPTED |
| UNAVAILABLE | AVAILABLE, FOSTERED |

### 2. Application Layer (`application/`)

Orchestrates use-cases. Defines **ports** (interfaces) that decouple the domain from infrastructure, and **commands** (CQRS write-side objects) with Jakarta Bean Validation.

| File | Purpose |
|------|---------|
| `port/in/PetInputPort.java` | **Driving port** — what the outside world (controllers) can ask the application to do: `create`, `update`, `delete`, `findById`, `findAll`, `findAvailable`, `changeStatus`. |
| `port/out/PetOutputPort.java` | **Driven port** — what persistence capabilities the application needs. Same shape as `PetRepository` but kept as a separate interface so the application layer never depends on the domain repository directly. |
| `command/CreatePetCommand.java` | Write command with validation: `@NotBlank name`, `@NotBlank species`, `@Min(0) @Max(600) age`, `@DecimalMin/@DecimalMax weight`, `@PastOrPresent rescueDate`, etc. |
| `command/UpdatePetCommand.java` | Partial-update command (all fields optional). |
| `usecase/PetUseCase.java` | **Implements `PetInputPort`.** Maps commands → domain `Pet` objects, then delegates to `PetDomainService`. This is the application's orchestration layer. |
| `dto/PetDto.java` | Read-side DTO returned by queries. Includes computed `ageDisplay` field (e.g. "2 years 3 months"). |

### 3. Infrastructure Layer (`infrastructure/`)

Adapters for external concerns — database, security, logging, serialization, API docs.

#### Persistence (`infrastructure/persistence/`)

| File | Purpose |
|------|---------|
| `PetJpaEntity.java` | JPA `@Entity` mapped to the `pets` table. Has all JPA annotations (`@Table`, `@Index`, `@Enumerated`, `@Column`, `@ElementCollection` for image URLs) and audit fields via `@EntityListeners(AuditingEntityListener.class)`. |
| `PetJpaRepository.java` | Spring Data JPA repository. Custom JPQL queries: `findByIdAndNotDeleted` and `findAllNotDeleted` filter out soft-deleted records; `findByStatus` filters by pet status. |
| `PetEntityMapper.java` | MapStruct mapper: domain `Pet` ↔ JPA `PetJpaEntity`. Generates implementation at compile time. |
| `PetRepositoryAdapter.java` | **Adapter** implementing both `PetRepository` (domain) and `PetOutputPort` (application). Bridges domain operations to Spring Data via the mapper. No `@Transactional` — transactions are managed at the domain service layer. |

#### Configuration (`infrastructure/config/`)

| File | Purpose |
|------|---------|
| `SecurityConfig.java` | **Skeleton** — permits all requests, stateless sessions, CSRF disabled. In production you'd add JWT filters and role-based access here. |
| `JpaAuditingConfig.java` | Enables `@EnableJpaAuditing`. Returns `"system"` as the auditor for `@CreatedBy` / `@LastModifiedBy` fields. |
| `JacksonConfig.java` | Configures Jackson: `JavaTimeModule` for Java 8 date/time, writes dates as ISO strings (not timestamps), excludes null fields, ignores unknown properties on deserialization. |
| `OpenApiConfig.java` | `@OpenAPIDefinition` with API title, version, and server URL for Swagger UI. |
| `CorrelationIdFilter.java` | **Highest precedence filter.** Reads or generates `X-Correlation-ID` header, stores it in MDC (for structured logging), and adds it to the response. |
| `LoggingFilter.java` | Logs incoming requests and outgoing responses with latency. Skips actuator and swagger paths to reduce noise. |

### 4. Presentation Layer (`presentation/`)

REST API delivery mechanism — controllers, exception handling, and web mappers.

| File | Purpose |
|------|---------|
| `controller/PetController.java` | REST endpoints at `/api/v1/pets`. Operations: `POST /` (create), `GET /` (list all), `GET /{id}` (get one), `GET /available` (list available), `PUT /{id}` (update), `PATCH /{id}/status?status=X` (change status), `DELETE /{id}` (soft-delete). All responses use a `{ "success": true, "data": ... }` envelope. |
| `advice/GlobalExceptionAdvice.java` | `@RestControllerAdvice` — uniform error JSON envelope. Handles: `BaseException` (domain errors), `MethodArgumentNotValidException` (validation), `ConstraintViolationException`, `IllegalStateException` (bad status transitions), and a catch-all for unexpected errors. |
| `mapper/PetWebMapper.java` | MapStruct mapper: domain `Pet` → `PetDto`. Includes a `formatAge()` default method that converts months to human-readable text (e.g., `24` → `"2 years"`). |

---

## API Endpoints

All endpoints are under `/api/v1/pets`:

| Method | Path | Description | Request Body |
|--------|------|-------------|-------------|
| `POST` | `/api/v1/pets` | Create a new pet | `CreatePetCommand` (JSON) |
| `GET` | `/api/v1/pets` | List all pets | — |
| `GET` | `/api/v1/pets/{id}` | Get pet by ID | — |
| `GET` | `/api/v1/pets/available` | List available pets | — |
| `PUT` | `/api/v1/pets/{id}` | Update a pet | `UpdatePetCommand` (JSON) |
| `PATCH` | `/api/v1/pets/{id}/status?status=PENDING` | Change pet status | — (query param) |
| `DELETE` | `/api/v1/pets/{id}` | Soft-delete a pet | — |

### Example: Create a pet

```bash
curl -X POST http://localhost:8080/api/v1/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buddy",
    "species": "Dog",
    "breed": "Golden Retriever",
    "age": 24,
    "gender": "MALE",
    "color": "Golden",
    "weight": 30.5,
    "description": "A friendly, well-trained family dog",
    "vaccinated": true,
    "neutered": true,
    "rescueDate": "2024-01-15",
    "rescueLocation": "Central Park"
  }'
```

Response:

```json
{
  "success": true,
  "data": {
    "id": "441963ab-f1e2-4472-8a6e-068238371404",
    "name": "Buddy",
    "species": "Dog",
    "breed": "Golden Retriever",
    "age": 24,
    "ageDisplay": "2 years",
    "gender": "MALE",
    "color": "Golden",
    "weight": 30.5,
    "description": "A friendly, well-trained family dog",
    "status": "AVAILABLE",
    "vaccinated": true,
    "neutered": true,
    "rescueDate": "2024-01-15",
    "rescueLocation": "Central Park",
    "imageUrls": [],
    "createdAt": "2024-01-15T10:30:00.000"
  }
}
```

---

## Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Spring Boot 3.5** | Application framework |
| **Java 21** | Language (Gradle toolchain) |
| **Spring Data JPA** | Repository abstraction |
| **PostgreSQL** | Database (Docker for local, NeonDB for prod) |
| **PostgreSQL driver** | Included for production deployment |
| **Spring Security** | Skeleton (permit all) |
| **MapStruct 1.6** | Compile-time DTO/entity mapping |
| **Lombok** | Boilerplate reduction |
| **springdoc-openapi 2.8** | Swagger UI + OpenAPI spec |
| **Spring Boot Actuator** | Health & info endpoints |
| **Logback** | Logging with correlation IDs |

---

## Project Structure

```
pet-rescue-api/
├── build.gradle.kts                 # Gradle build (Kotlin DSL)
├── settings.gradle.kts              # Gradle settings
├── gradlew / gradlew.bat           # Gradle wrapper scripts
├── src/
│   ├── main/
│   │   ├── java/com/uit/petrescueapi/
│   │   │   ├── PetRescueApiApplication.java      # Spring Boot entry point
│   │   │   │
│   │   │   ├── domain/                            # ① DOMAIN LAYER
│   │   │   │   ├── entity/
│   │   │   │   │   └── Pet.java                   #    Aggregate root (pure POJO)
│   │   │   │   ├── valueobject/
│   │   │   │   │   ├── PetStatus.java             #    Status enum
│   │   │   │   │   ├── Gender.java                #    Gender enum
│   │   │   │   │   └── HealthStatus.java          #    Health status enum
│   │   │   │   ├── repository/
│   │   │   │   │   └── PetRepository.java         #    Domain port (interface)
│   │   │   │   ├── service/
│   │   │   │   │   └── PetDomainService.java      #    Business rules + transactions
│   │   │   │   └── exception/
│   │   │   │       ├── BaseException.java         #    Abstract base exception
│   │   │   │       ├── ResourceNotFoundException.java
│   │   │   │       └── BusinessException.java
│   │   │   │
│   │   │   ├── application/                       # ② APPLICATION LAYER
│   │   │   │   ├── port/
│   │   │   │   │   ├── in/
│   │   │   │   │   │   └── PetInputPort.java      #    Driving port (interface)
│   │   │   │   │   └── out/
│   │   │   │   │       └── PetOutputPort.java     #    Driven port (interface)
│   │   │   │   ├── command/
│   │   │   │   │   ├── CreatePetCommand.java      #    Write command + validation
│   │   │   │   │   └── UpdatePetCommand.java      #    Update command
│   │   │   │   ├── usecase/
│   │   │   │   │   └── PetUseCase.java            #    Implements PetInputPort
│   │   │   │   └── dto/
│   │   │   │       └── PetDto.java                #    Read-side DTO
│   │   │   │
│   │   │   ├── infrastructure/                    # ③ INFRASTRUCTURE LAYER
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── PetJpaEntity.java          #    JPA entity (@Entity)
│   │   │   │   │   ├── PetJpaRepository.java      #    Spring Data repository
│   │   │   │   │   ├── PetEntityMapper.java       #    MapStruct: domain ↔ JPA
│   │   │   │   │   └── PetRepositoryAdapter.java  #    Adapter: domain port → JPA
│   │   │   │   └── config/
│   │   │   │       ├── SecurityConfig.java        #    Permit-all skeleton
│   │   │   │       ├── JpaAuditingConfig.java     #    @EnableJpaAuditing
│   │   │   │       ├── JacksonConfig.java         #    JSON serialization
│   │   │   │       ├── OpenApiConfig.java         #    Swagger/OpenAPI setup
│   │   │   │       ├── CorrelationIdFilter.java   #    Request correlation IDs
│   │   │   │       └── LoggingFilter.java         #    Request/response logging
│   │   │   │
│   │   │   └── presentation/                      # ④ PRESENTATION LAYER
│   │   │       ├── controller/
│   │   │       │   └── PetController.java         #    REST endpoints /api/v1/pets
│   │   │       ├── advice/
│   │   │       │   └── GlobalExceptionAdvice.java #    Uniform error handling
│   │   │       └── mapper/
│   │   │           └── PetWebMapper.java          #    MapStruct: domain → DTO
│   │   │
│   │   └── resources/
│   │       ├── application.properties             # Default (PostgreSQL via Docker)
│   │       ├── application-production.properties  # NeonDB (cloud Postgres)
│   │       └── logback-spring.xml                 # Console logging with correlation ID
│   │
│   └── test/
│       └── java/.../PetRescueApiApplicationTests.java
│
├── Dockerfile                       # Multi-stage build (JDK 21)
├── docker-compose.yml               # Local dev: app + Postgres
├── docker-compose.prod.yml          # Production: app only (NeonDB)
├── .env.example                     # Environment variable template
├── .dockerignore
│
└── .github/
    ├── copilot-instructions.md      # AI coding agent guidance
    └── workflows/
        ├── ci.yml                   # Build & test on every push/PR
        └── deploy.yml               # Build image → push → deploy to prod
```

---

## Environment Profiles

The app uses Spring profiles to switch databases:

| Profile | Activated by | Database | Use case |
|---------|-------------|----------|----------|
| *(default)* | `./gradlew bootRun` | **Local PostgreSQL** (Docker) | Local dev |
| `production` | `SPRING_PROFILES_ACTIVE=production` | **NeonDB** (cloud Postgres) | Deployment |

### Profile config files

- `application.properties` — base config + local Postgres defaults
- `application-production.properties` — overrides datasource to NeonDB with SSL, tuned connection pool

---

## Docker

### Architecture

```
┌─ docker-compose.yml (LOCAL DEV) ─────────────────┐
│                                                    │
│  ┌──────────┐       ┌───────────────────┐         │
│  │ Postgres │◄──────│  Spring Boot App  │         │
│  │ :5432    │       │  :8080            │         │
│  └──────────┘       └───────────────────┘         │
└────────────────────────────────────────────────────┘

┌─ docker-compose.prod.yml (DEPLOY) ───────────────┐
│                                                    │
│  ┌───────────────────┐       ┌──────────────────┐ │
│  │  Spring Boot App  │──────►│  NeonDB (cloud)  │ │
│  │  :8080            │       │  *.neon.tech     │ │
│  └───────────────────┘       └──────────────────┘ │
│                          NO Postgres container     │
└────────────────────────────────────────────────────┘
```

### Dockerfile

Multi-stage build:
1. **Builder** — uses `eclipse-temurin:21-jdk-alpine`, runs `gradlew bootJar`
2. **Runtime** — uses `eclipse-temurin:21-jre-alpine`, runs as non-root user, includes health check

### docker-compose.yml (local dev)

Starts **both** the Spring Boot app and a PostgreSQL 16 container.
The app container uses profile `development` and connects to the local Postgres.

```bash
docker-compose up -d          # start
docker-compose logs -f app    # follow logs
docker-compose down -v        # stop + remove volumes
```

### docker-compose.prod.yml (deployment)

Starts **only** the Spring Boot app. No Postgres container — connects to NeonDB via environment variables.

```bash
# Requires DB_* env vars pointing to NeonDB
docker compose -f docker-compose.prod.yml up -d
```

---

## CI/CD

Two GitHub Actions workflows in `.github/workflows/`:

### ci.yml — Continuous Integration

- **Triggers**: Push to `main` or `develop`, PR to `main`
- **Steps**: Checkout → Setup JDK 21 → `./gradlew build` (compile + test) → Upload test reports
- Runs on every push/PR to catch broken builds early

### deploy.yml — Build Image + Deploy

- **Triggers**: Push to `main` only
- **Jobs**:
  1. **build** — Build & test with Gradle
  2. **docker** — Build Docker image, push to GitHub Container Registry (GHCR)
  3. **deploy** — SSH into production server, pull image, run `docker-compose.prod.yml`

**Key point**: The deploy job uses `docker-compose.prod.yml` which has **no Postgres container** — it connects to NeonDB via secrets.

### Required GitHub Secrets

| Secret | Purpose |
|--------|--------|
| `DEPLOY_HOST` | Production server hostname/IP |
| `DEPLOY_USER` | SSH user |
| `DEPLOY_SSH_KEY` | SSH private key |
| `DEPLOY_PATH` | Path to docker-compose.prod.yml on server |
| `NEONDB_HOST` | e.g. `ep-cool-name-123456.us-east-2.aws.neon.tech` |
| `NEONDB_PORT` | `5432` |
| `NEONDB_NAME` | Database name |
| `NEONDB_USERNAME` | NeonDB user |
| `NEONDB_PASSWORD` | NeonDB password |

---

## NeonDB Setup

1. Create a project at [neon.tech](https://neon.tech)
2. Copy the connection details from the dashboard
3. Set the `NEONDB_*` secrets in GitHub repo settings → Secrets and variables → Actions
4. The `production` profile uses `?sslmode=require` (NeonDB requires SSL)
5. Schema is auto-created by Hibernate `ddl-auto=update`

---

## How to Extend

1. **Add a new entity** (e.g., Shelter): Create `domain/entity/Shelter.java`, a domain repository interface, a domain service, then wire up application ports/use-cases, infrastructure JPA entity/adapter, and a presentation controller.
2. **Add authentication**: Replace `SecurityConfig`'s permit-all with JWT filters. Add `JwtService`, `JwtAuthenticationFilter`, user entities, and update `SecurityConfig.PUBLIC_ENDPOINTS`.
3. **Switch to PostgreSQL locally**: Run `docker-compose up -d` — it already includes Postgres.
4. **Add a new business rule**: Put it in `PetDomainService` (or the relevant domain service). The domain layer is the single source of truth for business logic.

---

## License

This project is for educational purposes.

