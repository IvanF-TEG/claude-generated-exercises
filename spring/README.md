# Spring Boot Track: FreightBoard (SB01–SB10)

## Summary
Ten hands-on exercises that build **FreightBoard**, a REST API for a freight exchange: shippers post loads, carriers bid on them, and a shipper accepts one. Each exercise adds one layer of a real Spring Boot service: endpoints, dependency injection, configuration, CRUD, validation, a database, relationships and transactions, migrations on PostgreSQL, security, and production readiness. It's the same format as Batch 2: a primer with Python comparisons, **fewer, bigger TODOs**, JUnit tests grouped by TODO, predictions, three-level hints and a Definition of Done.

**Stack:** Java 21 · Spring Boot 4.1.1 (Spring Framework 7, Spring Security 7, Hibernate 7, Jackson 3) · Maven · H2 · PostgreSQL 17 · Flyway · Testcontainers · Docker

## Key Details

### Entry criteria (definition of ready)
- **Batch 2 is Done.** Spring code leans on interfaces (Ex 13), lambdas and method references (Ex 16), streams and `Optional` (Ex 17–18) and records (Ex 19) on almost every page.
- For SB08 and SB10: **Docker Desktop** installed and running (`docker info` works). Everything else runs without it.

> ⚠️ **Blocker to watch:** this track was generated while Batch 1 (Ex 08–10) and Batch 2 were still open. Keep WIP at 1: finish those first, and give feedback on Batch 2 before pulling SB01. The feedback may change how the later exercises should be pitched.

### Progress board (WIP limit: 1)
| Backlog | Ready | In Progress | Blocked | Done |
|---|---|---|---|---|
| SB02 Dependency Injection | **SB01** First Endpoints *(once Batch 2 is done)* | | | |
| SB03 Configuration | | | | |
| SB04 REST CRUD | | | | |
| SB05 Validation & Errors | | | | |
| SB06 JPA | | | | |
| SB07 Relationships & Transactions | | | | |
| SB08 Flyway & PostgreSQL | | | | |
| SB09 Security | | | | |
| SB10 Production Readiness | | | | |

**Blocked rule:** if you're stuck for more than 20 minutes, reveal the next hint. If you're still stuck after Hint 3, move the card to *Blocked*, write down why, and ask for help.

### Curriculum map
| # | Exercise | New concept | Tests | Est. time | Needs Docker? |
|---|---|---|---|---|---|
| SB01 | [First Endpoints](sb01-first-endpoints/README.md) | HTTP & REST primer, `@RestController`, path/query/body input, `ResponseEntity` | 39 | 75–90 min | |
| SB02 | [Dependency Injection](sb02-dependency-injection/README.md) | Beans, constructor injection, `@Configuration`/`@Bean`, `@Primary`, manual wiring first | 66 | 75–90 min | |
| SB03 | [Configuration](sb03-configuration/README.md) | YAML, `@ConfigurationProperties` records, `@Value`, profiles, precedence, fail-fast | 86 | 75–90 min | |
| SB04 | [REST CRUD](sb04-rest-crud/README.md) | Resource design, 201/204/404, three layers, `@WebMvcTest` + Mockito | 113 | 75–90 min | |
| SB05 | [Validation & Errors](sb05-validation-errors/README.md) | Bean Validation, custom constraint, `@RestControllerAdvice`, `ProblemDetail` | 124 | 75–90 min | |
| SB06 | [JPA](sb06-jpa/README.md) | SQL primer, entities, Spring Data derived queries, JPQL, dirty checking, DTOs | 137 | 90 min | |
| SB07 | [Relationships & Transactions](sb07-relationships-transactions/README.md) | `@ManyToOne`/`@OneToMany`, lazy loading, all-or-nothing, rollback rules, N+1 | 168 | 90–120 min | |
| SB08 | [Flyway & PostgreSQL](sb08-flyway-postgres/README.md) | Versioned migrations, backfills, `ddl-auto: validate`, Testcontainers | 182 | 90 min | For 3 tests |
| SB09 | [Security](sb09-security/README.md) | AuthN vs AuthZ, bcrypt, URL rules, ownership (BOLA), method security | 232 | 90–120 min | For 3 tests |
| SB10 | [Production Readiness](sb10-production/README.md) | Actuator, health, metrics, events, logging + MDC, JSON logs, Dockerfile | 251 | 90–120 min | For the image |

Test counts are cumulative: each module keeps every earlier test, and they must stay green. **Total:** roughly 15–18 hours. At one exercise per sitting, that's about 2–3 weeks.

### How the modules fit together
- Every module is a **complete, standalone Spring Boot app** in its own Maven module, all under this `spring/` parent.
- **Each module's starter code is the previous module's finished solution**, plus new TODOs. If you get stuck on SB05, SB06 still starts from a working SB05.
- ⚠️ **Spoiler warning:** that means `sbNN+1` contains the answers to `sbNN`. Don't open the next module until you've finished the current one.
- Comments in inherited code like `SB04 step 2b:` mark work done in an earlier exercise. Only lines marked `TODO` are yours.
- Package-by-feature throughout: `com.freightboard.loads`, `.bids`, `.carriers`, `.quotes`, `.security`, `.ops`...

### What's in each exercise
- `README.md`: learning objective, primer, tasks, how to run, test cases, Definition of Done
- `HINTS.md`: three progressive hints per TODO (syntax → approach → code)
- `src/main/...`: the app, with `TODO` markers
- `src/test/...`: the tests, grouped by TODO (`@Nested class Todo3...` or methods named `todo3...`), plus `PredictionsTest` (fill in *before* running)

### Setup (one time only)
- **IntelliJ:** right-click `spring/pom.xml` → **Add as Maven Project**, and wait for the downloads (a few hundred MB the first time). Each module then appears in the Maven tool window. Click ▶ next to any test class or `@Nested` group, or run a module's `FreightBoardApplication`.
- **Terminal** (from the `spring` folder):
  ```bash
  mvn -q test -pl sb01-first-endpoints                          # one exercise
  mvn -q test -pl sb01-first-endpoints -Dtest='PostcodeInfoTest' # one test class
  mvn -q spring-boot:run -pl sb01-first-endpoints               # run the app on http://localhost:8080
  ```
- Run **one module at a time** with `-pl`. A plain `mvn test` in `spring/` builds all ten, and stops at the first module with failing tests, which is every unfinished one.

### Recurring themes to watch for
| Theme | Where |
|---|---|
| Plain Java first, framework second | SB01 (logic in records) → SB02 (manual wiring) → SB05 (validation without Spring) |
| Test at the right level: unit → slice → full app | SB04 (`@WebMvcTest`) → SB06 (`@DataJpaTest`) → SB08 (real PostgreSQL) |
| HTTP status codes as an API contract | SB01 (400) → SB04 (201/204/404) → SB05 (409, problem details) → SB09 (401/403) |
| Proxies: the magic behind annotations | SB06–07 (`@Transactional`) → SB09 (`@PreAuthorize`) → SB10 (`@TransactionalEventListener`) |
| Never trust the client | SB05 (validate input) → SB09 (identity from the login, ownership checks) |
| Secrets and personal data (GDPR) | SB03 (no secrets in YAML) → SB08 (DB passwords) → SB09 (users) → SB10 (no personal data in logs) |
| Money in pence (`long`) | SB02 onwards, as in Batches 1 and 2 |

## Recommended Next Steps
| Action | Owner |
|---|---|
| Finish Batch 1 (Ex 08–10), then Batch 2 (Ex 11–20) | You |
| Give feedback on Batch 2 (difficulty, pacing, TODO size) before starting SB01 | You |
| Check your team's Spring Boot version, database and build tool; adjust if they differ from 4.1 / PostgreSQL / Maven | You, with a colleague |
| Install Docker Desktop before SB08 | You |
| After SB10: choose the next topic (interview-style algorithms, or Spring follow-ups: OAuth2/JWT, messaging, caching, reactive) | You → ask Claude |
