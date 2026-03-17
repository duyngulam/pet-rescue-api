# Copilot instructions for Pet Rescue API

## Purpose
Help AI agents become productive quickly in this repository.

## Architecture
- **Clean Architecture** with 4 layers: `domain` → `application` → `infrastructure` / `presentation`.
- Dependencies point inward only. `domain` has zero framework dependencies (only Lombok).
- **CQRS** for Pet: separate command path (through domain service) and query path (bypasses domain, uses JPQL projections).
- **JWT authentication** with refresh-token rotation, email verification, RBAC (SystemRole: USER/ADMIN/MEMBER).

## Package layout
```
com.uit.petrescueapi
├── domain/
│   ├── entity/         Pet, User, Role, Organization, RefreshToken, EmailVerificationToken
│   ├── valueobject/    PetStatus, Gender, HealthStatus, UserStatus, SystemRole, OrganizationRole
│   ├── repository/     Domain ports (interfaces): PetRepository, UserRepository, RoleRepository, etc.
│   ├── service/        PetDomainService, UserDomainService, AuthDomainService
│   └── exception/      BaseException, ResourceNotFoundException, BusinessException, etc.
├── application/
│   ├── port/
│   │   ├── command/    PetCommandPort, AuthCommandPort + 9 more (write interfaces)
│   │   ├── query/      PetQueryPort + 9 more (read interfaces, return DTOs)
│   │   └── out/        PetQueryDataPort (output port for CQRS read-side adapter)
│   ├── usecase/        PetCommandUseCase, PetQueryUseCase, AuthCommandUseCase
│   └── dto/            pet/, auth/, user/, organization/, adoption/, post/, etc.
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/     *JpaEntity classes (JPA annotations)
│   │   ├── repository/ PetJpaRepository (command), PetQueryJpaRepository (query JOIN), etc.
│   │   ├── mapper/     MapStruct: domain ↔ JPA entity
│   │   ├── adapter/    PetRepositoryAdapter (command), PetQueryAdapter (query), etc.
│   │   └── projection/ PetSummaryProjection, PetDetailProjection (interface projections)
│   ├── config/         SecurityConfig, JpaAuditingConfig, JacksonConfig, OpenApiConfig, filter/
│   ├── security/       JwtService, JwtAuthenticationFilter, CustomUserDetailsService
│   └── email/          EmailService (@Async verification emails)
└── presentation/
    ├── controller/     AuthController, PetController, DevController, mock/ (9 mock controllers)
    ├── advice/         GlobalExceptionAdvice
    ├── dto/            ApiResponse, PageResponse
    └── mapper/         PetWebMapper (MapStruct: domain → DTO, used by command path only)
```

## CQRS flow (Pet)
```
COMMAND: Controller → PetCommandPort → PetCommandUseCase → PetDomainService → PetRepository → PetRepositoryAdapter → PetJpaRepository
QUERY:   Controller → PetQueryPort → PetQueryUseCase → PetQueryDataPort → PetQueryAdapter → PetQueryJpaRepository (LEFT JOIN projections)
```
- **Query path bypasses domain entirely.** PetQueryAdapter maps interface projections → application DTOs directly.
- Organization data is always included via `LEFT JOIN` in query SQL — no N+1, no separate enrichment flag.

## Build / run / test
- Build: `./gradlew build` (Windows: `gradlew.bat build`). Requires JDK 21+.
- Run (local dev): `docker-compose up -d` then `./gradlew bootRun` — starts on port 8080.
- Run (full Docker): `docker-compose --profile docker up -d --build` — starts app + PostgreSQL in containers.
- Reset DB: `docker-compose down -v`
- Tests: `./gradlew test` (uses H2 in-memory, no Docker needed).

## Profiles
- *(default / development)*: local PostgreSQL via Docker
- `production`: NeonDB (cloud Postgres) — `docker-compose.prod.yml`

## Key files
| Concern | File |
|---------|------|
| Entry point | `PetRescueApiApplication.java` |
| Pet business rules | `domain/service/PetDomainService.java` |
| Auth orchestration | `application/usecase/AuthCommandUseCase.java` |
| Pet command use-case | `application/usecase/PetCommandUseCase.java` |
| Pet query use-case | `application/usecase/PetQueryUseCase.java` |
| Query output port | `application/port/out/PetQueryDataPort.java` |
| Query adapter (JOIN) | `infrastructure/persistence/adapter/PetQueryAdapter.java` |
| Query JPA repo | `infrastructure/persistence/repository/PetQueryJpaRepository.java` |
| Interface projections | `infrastructure/persistence/projection/PetSummaryProjection.java`, `PetDetailProjection.java` |
| Command adapter | `infrastructure/persistence/adapter/PetRepositoryAdapter.java` |
| REST endpoints | `presentation/controller/PetController.java`, `AuthController.java` |
| Error handling | `presentation/advice/GlobalExceptionAdvice.java` |
| JWT service | `infrastructure/security/JwtService.java` |
| Security config | `infrastructure/config/SecurityConfig.java` |
| Swagger/OpenAPI | `infrastructure/config/OpenApiConfig.java` |
| App config | `src/main/resources/application.properties` |

## Conventions
- Controllers: `/api/v1/**` URI pattern. Responses wrapped in `ApiResponse<T>`: `{ "success": true, "data": ... }`.
- `@Transactional` only on domain services — never on adapters or controllers.
- MapStruct for all mapping (domain ↔ JPA entity, domain → DTO). Annotation processor order: lombok → lombok-mapstruct-binding → mapstruct-processor.
- Soft-delete pattern: `deleted`, `deletedAt`, `deletedBy` fields in `BaseEntity`.
- Status transitions validated via `ALLOWED_TRANSITIONS` map in `PetDomainService`.
- Query ports return application DTOs directly (not domain entities).
- Organization data joined in SQL — never fetched separately for list/detail views.

### Pagination
- **Every GET list endpoint** must be paginated — no unpaginated collection endpoints.
- Use `PageResponse<T>` (in `presentation/dto/`) as the pagination envelope, wrapped in `ApiResponse<PageResponse<T>>`.
- Default parameters: `@RequestParam(defaultValue = "0") int page`, `@RequestParam(defaultValue = "20") int size`.
- Query path: `PageRequest.of(page, size)` passes through port → use-case → query adapter; JPA returns `Page<Projection>`, adapter maps to `Page<DTO>`.
- In mock controllers, construct `PageResponse` manually with mock values.

## When adding code

### New entity
1. Create in `domain/entity/`, add domain repository interface in `domain/repository/`.
2. Add domain service in `domain/service/` (business rules + `@Transactional`).
3. Wire application ports (`port/command/`, `port/query/`) and use-cases.
4. Create JPA entity in `infrastructure/persistence/entity/`, mapper, JPA repository, and adapter.
5. Add presentation controller.

### New query endpoint (CQRS read-side)
1. Add interface projection in `infrastructure/persistence/projection/`.
2. Add `@Query` method with `LEFT JOIN` in `PetQueryJpaRepository`.
3. Add mapping method in `PetQueryAdapter` (projection → DTO).
4. Expose via `PetQueryDataPort` → `PetQueryPort` → controller.

### General rules
- Keep `domain` free of framework imports (no Spring, no JPA annotations). Only Lombok allowed.
- Put JPA annotations only on `infrastructure/persistence/entity/*JpaEntity` classes.
- Use the Gradle wrapper (`gradlew` / `gradlew.bat`) — toolchain is Java 21.

### CQRS & Transaction boundaries (CRITICAL)
**ALL production controllers MUST follow this pattern (see PetController, AuthController, OrganizationController):**

**Command path (writes):**
1. Controller → `*CommandPort` (use-case interface) → `*CommandUseCase` → `*DomainService` (@Transactional) → domain repository → adapter → JPA repo
2. Command use-cases are **thin orchestrators** — NO business logic, only DTO → domain mapping
3. ALL business rules + `@Transactional` live in domain service ONLY
4. Command ports **return domain entities** (not DTOs) — controller maps via `*WebMapper` (MapStruct)
5. For multi-entity operations (e.g., Pet + PetOwnership), domain service creates BOTH in one transaction

**Query path (reads):**
1. Controller → `*QueryPort` (use-case interface) → `*QueryUseCase` → `*QueryDataPort` (output port) → `*QueryAdapter` → `*QueryJpaRepository` (JOIN projections)
2. Query path **bypasses domain layer entirely** (pure CQRS)
3. Query use-cases are **thin delegators** — just call query data port
4. Query ports **return DTOs directly** (not domain entities)
5. Query adapters map interface projections → application DTOs

**Transaction rules:**
- `@Transactional` ONLY on domain services — never on use-cases, adapters, or controllers
- Domain service methods are atomic — one call = one transaction
- For read-only queries, use `@Transactional(readOnly = true)` on domain service query methods or query adapters
- Controllers never handle transactions — they orchestrate ports only
- **CRITICAL for Spring Security**: `UserDetailsService.loadUserByUsername()` MUST have `@Transactional(readOnly = true)` to keep Hibernate session open while accessing lazy-loaded collections (e.g., user roles). Without it, you'll get `LazyInitializationException: no Session`.

**Why command returns entity, query returns DTO:**
- Commands change state → need domain invariants → return entity for continued operations or mapping
- Queries read state → bypass domain for performance → return optimized DTO directly from JOIN projection
- Separation allows independent optimization of read vs write paths

**Controllers must use ports, NOT domain services directly:**
```java
// ✅ CORRECT — production domain controllers
@RestController
@RequiredArgsConstructor
public class PetController {
    private final PetCommandPort commandPort;  // Write operations
    private final PetQueryPort queryPort;      // Read operations
    private final PetWebMapper mapper;         // Domain → DTO (command path only)

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponseDto>> create(@RequestBody CreatePetRequestDto cmd) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(commandPort.create(cmd))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(queryPort.findById(id)));  // DTO returned directly
    }
}

// ❌ INCORRECT — violates Clean Architecture
@RestController
@RequiredArgsConstructor
public class BadController {
    private final PetDomainService domainService;  // ❌ Never inject domain services in production controllers
}
```

**Acceptable CQRS exceptions (2 controllers only):**
Two controllers intentionally bypass CQRS for valid architectural reasons:

1. **`DevController`** (@Profile("dev")) — Development utility for bootstrapping (creating admins, seeding data). Never deployed to production. Directly injects `UserDomainService` to create pre-verified accounts.

2. **`AdminController`** (@PreAuthorize("ADMIN")) — Privileged administrative operations that bypass normal business flows by design (force-create users, override permissions, assign roles without requests). These are **tactical maintenance operations**, not domain use cases.

**General guideline:**
- **Domain controllers** (Pet, Auth, Organization, Adoption, Post, RescueCase, Media, etc.) → **MUST use CQRS ports**
- **Operational controllers** (Dev, Admin) → **MAY directly inject domain services** if the operation is truly outside normal business flows (document why in comments)

**Dependency direction (Dependency Inversion Principle):**
- Presentation → Application (ports) → Domain ← Application (adapters) ← Infrastructure
- Controllers NEVER depend on domain services directly (except Dev/Admin)
- Controllers NEVER depend on infrastructure layer (JPA repositories, entities, etc.)
- Domain defines interfaces (repository ports); infrastructure implements them

### Audit fields & naming conventions (CRITICAL)
**ALL JPA entities MUST include full audit fields** matching `BaseEntity`:
- `createdAt` (TIMESTAMPTZ, not null) — automatically set on insert
- `createdBy` (VARCHAR(255)) — user/system identifier
- `updatedAt` (TIMESTAMPTZ) — automatically set on insert/update
- `updatedBy` (VARCHAR(255)) — user/system identifier
- `deleted` (BOOLEAN, not null, default false) — soft-delete flag (column name: `is_deleted`)
- `deletedAt` (TIMESTAMPTZ) — timestamp of deletion
- `deletedBy` (VARCHAR(255)) — who performed the deletion

**Naming conventions:**
- Java field names: **camelCase** (e.g., `streetAddress`, `wardCode`, `organizationId`)
- DB column names: **snake_case** (e.g., `street_address`, `ward_code`, `organization_id`)
- Map with `@Column(name = "snake_case_name")` annotation
- NEVER mix snake_case in Java fields — use `@Column` to bridge to DB

**When creating new entities:**
1. Domain entity extends `BaseEntity` (abstract class with audit fields as protected fields)
2. JPA entity includes all audit fields with `@Column` mappings
3. Use `@EntityListeners(AuditingEntityListener.class)` + `@CreatedDate/@CreatedBy/@LastModifiedDate/@LastModifiedBy` for automatic population
4. Database migration MUST include all 7 audit columns for every table
5. For junction/relationship tables, evaluate if full audit is needed (default: yes for consistency)

### Query optimization — always avoid N+1
- **Never** call a repository method inside a loop or stream. Batch-fetch with `findAllByIdIn()` and collect into a `Map<UUID, Entity>` instead.
- **Every list/detail JPQL query** must use explicit `SELECT o.field AS alias` projections — never `SELECT o` (avoids SELECT *).
- **JOIN related data in SQL**, not in Java. Use `JOIN` (or `LEFT JOIN`) in the `@Query` and map the joined columns to the interface projection directly. This keeps list queries to a single round-trip.
- **Interface projections** (not entity projections) are preferred for read-side queries — they return only the columns the client needs and work well with Spring Data Tuple mapping.
- For `@ManyToOne` associations used only in JOIN queries (not for writes), declare the association with `insertable = false, updatable = false` alongside the owning `@Id` / `@Column` field.
- Count queries for pagination must be provided explicitly when the main query contains a JOIN, to avoid Hibernate generating an incorrect count query:
  ```java
  @Query(value = "SELECT ... FROM ... JOIN ...", countQuery = "SELECT COUNT(o.id) FROM ...")
  ```

## URLs (when running locally)
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/api-docs
- Actuator health: http://localhost:8080/actuator/health
