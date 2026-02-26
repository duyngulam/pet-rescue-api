# Copilot instructions for Pet Rescue API

## Purpose
Help AI agents become productive quickly in this repository.

## Architecture
- **Clean Architecture** with 4 layers: `domain` → `application` → `infrastructure` / `presentation`.
- Dependencies point inward only. `domain` has zero framework dependencies.
- Single flow: **Pet CRUD** (no auth, no user/role management).

## Package layout
```
com.uit.petrescueapi
├── domain/           entity, valueobject, repository (port), service, exception
├── application/      port/in, port/out, command, usecase, dto
├── infrastructure/   persistence (JPA entity, mapper, adapter, repository), config
└── presentation/     controller, advice, mapper
```

## Build / run / test
- Build: `./gradlew build` (Windows: `gradlew.bat build`). Requires JDK 21+.
- Run: `docker-compose up -d postgres` then `./gradlew bootRun` — starts on port 8080.
- Run with Docker: `docker-compose up -d` — starts app + PostgreSQL.
- Tests: `./gradlew test` (uses H2 in-memory, no Docker needed).

## Profiles
- *(default)*: local PostgreSQL via Docker
- `production`: NeonDB (cloud Postgres) — `docker-compose.prod.yml`

## Key files
| Concern | File |
|---------|------|
| Entry point | `PetRescueApiApplication.java` |
| Business rules | `domain/service/PetDomainService.java` |
| Use-case orchestration | `application/usecase/PetUseCase.java` |
| REST endpoints | `presentation/controller/PetController.java` |
| Error handling | `presentation/advice/GlobalExceptionAdvice.java` |
| JPA adapter | `infrastructure/persistence/PetRepositoryAdapter.java` |
| Security (skeleton) | `infrastructure/config/SecurityConfig.java` |
| Swagger/OpenAPI | `infrastructure/config/OpenApiConfig.java` |
| App config | `src/main/resources/application.properties` |

## Conventions
- Controllers: `/api/v1/**` URI pattern. Responses wrapped in `{ "success": true, "data": ... }`.
- `@Transactional` only on `PetDomainService` — never on adapters or controllers.
- MapStruct for all mapping (domain ↔ JPA entity, domain → DTO).
- Lombok for boilerplate. Annotation processor order: lombok → lombok-mapstruct-binding → mapstruct-processor.
- Soft-delete pattern: `deleted`, `deletedAt`, `deletedBy` fields.
- Status transitions validated via `ALLOWED_TRANSITIONS` map in `PetDomainService`.

### Pagination
- **Every GET list endpoint** must be paginated — no unpaginated collection endpoints.
- Use `PageResponse<T>` (in `presentation/dto/`) as the pagination envelope, wrapped in `ApiResponse<PageResponse<T>>`.
- Default parameters: `@RequestParam(defaultValue = "0") int page`, `@RequestParam(defaultValue = "20") int size`.
- In the real Pet CRUD chain, pass `PageRequest.of(page, size)` through the port/use-case/domain-service layers; JPA queries return `Page<Entity>`.
- In mock controllers, construct `PageResponse` manually with mock `totalElements`, `totalPages`, `last` values.
- Domain repository / output port expose both `List<T> findAll()` (internal use) and `Page<T> findAll(Pageable)` (controller use) overloads.

## When adding code
- New entities: create in `domain/entity`, add domain port, domain service, then wire application ports/use-case, infrastructure adapter, and presentation controller.
- Keep `domain` free of framework imports (no Spring, no JPA annotations).
- Put JPA annotations only on `infrastructure/persistence/*JpaEntity` classes.
- Use the Gradle wrapper (`gradlew` / `gradlew.bat`) — toolchain is Java 21.

## URLs (when running locally)
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/api-docs
- Actuator health: http://localhost:8080/actuator/health
