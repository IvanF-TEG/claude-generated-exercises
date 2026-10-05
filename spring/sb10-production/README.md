# SB10: Production Readiness (Capstone)

**Time box:** 90–120 min · **New concept:** Spring Boot Actuator (health, info, metrics), a custom `HealthIndicator`, Micrometer counters, application events and `@TransactionalEventListener`, SLF4J logging, the MDC and request ids, structured (JSON) logs, executable jars, a layered Dockerfile
**Review:** SB03 profiles, SB07 transactions, SB09 security rules, SB08 Docker

## Learning Objective
FreightBoard works. Now make it **operable**: when it runs in production at 3 a.m., someone must be able to tell whether it's healthy, how busy it is, and what happened to one particular request, all without attaching a debugger. You'll finish by packaging the app the way it's actually shipped: an executable jar in a container image.

## Primer
**Actuator** adds operational endpoints under `/actuator`:
| Endpoint | Shows | Who should see it |
|---|---|---|
| `/actuator/health` | `UP`/`DOWN` (HTTP 200/503), plus details for each component (database, disk, your own checks) | Status: anyone (load balancers poll it). Details: admins |
| `/actuator/info` | App name, version, build time, from `info.*` and `build-info.properties` | Anyone |
| `/actuator/metrics` | Counters and timers: HTTP requests, JVM memory, database pool, and **your own** | Admins (or a monitoring system) |
| `env`, `beans`, `heapdump`, `shutdown`... | Configuration, internals | Nobody over HTTP: they can leak secrets |

**The three pillars of observability**
| | Answers | FreightBoard |
|---|---|---|
| **Health** | "Can this instance serve traffic?" | `LoadBoardHealthIndicator` |
| **Metrics** | "How much, how often, how fast?" (numbers over time) | `freightboard.bids.placed`, `http.server.requests` |
| **Logs** | "What exactly happened?" (events, with context) | `Bid 5 placed on load 7 ...` plus a request id |

**Logging well**
```java
private static final Logger log = LoggerFactory.getLogger(BidService.class);
log.info("Bid {} placed on load {}", bidId, loadId);      // {} placeholders: cheap when the level is off
log.info("Bid " + bidId + " placed on load " + loadId);   // builds the string even if nobody logs it
```
Python analogue: `logger = logging.getLogger(__name__)` and `logger.info("Bid %s placed", bid_id)`.

- **Levels:** `ERROR` (someone must act) > `WARN` (unexpected, but handled) > `INFO` (business events) > `DEBUG` (detail for developers).
- **The MDC** ("mapped diagnostic context") is a per-thread map whose values appear on every log line. Put a request id in it at the start of a request, and every line that request produces carries it. **Always clear it**, because server threads are reused.
- **Structured logs:** in production, one JSON object per line (`logging.structured.format.console: ecs`), so log tools can filter by field.
- **GDPR:** logs are copied widely and kept for a long time. Log **ids**, not names, usernames, emails or addresses.

**Events decouple.** `BidService` announces "a bid was placed" (`publishEvent`). It doesn't know about metrics, and metrics don't know about bidding. `@TransactionalEventListener` runs **after commit**, so a bid that rolls back is never counted. A plain `@EventListener` would run immediately, inside the transaction.

**Packaging.** `mvn package` builds `target/sb10-production-1.0-SNAPSHOT.jar`: one executable file that contains Tomcat, every library and your code. Run it with `java -jar`. A **layered** Dockerfile unpacks it, so dependency layers are cached between builds, and it runs as a **non-root** user.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `application.yml`, `security/SecurityConfig.java` | Expose health/info/metrics; details for admins; `info.app.*`; actuator security rules |
| 2 | `ops/LoadBoardHealthIndicator.java` | A custom health component: UP with the open-load count, DOWN with the error |
| 3 | `bids/BidService.java`, `ops/BidMetrics.java` | Publish events; count them **after commit** |
| 4 | `bids/BidService.java`, `application-prod.yml` | INFO log lines with placeholders and ids only; JSON logs in prod |
| 5 | `ops/RequestIdFilter.java`, `application.yml` | Request id → MDC → response header → log pattern; clear it in `finally` |
| 6 | `Dockerfile` | Two-stage, layered, non-root image |
| Predictions | `src/test/java/com/freightboard/ops/PredictionsTest.java` | Replace every `-1` / `"???"` *after* TODOs 1–5 but *before* running |

All 19 starter failures are SB10's own tests: everything from SB01–SB09 still passes.

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb10-production
mvn -q spring-boot:run -pl sb10-production
curl -s localhost:8080/actuator/health
curl -s -u admin:freight123 localhost:8080/actuator/health
curl -s localhost:8080/actuator/info
curl -s -u admin:freight123 localhost:8080/actuator/metrics/http.server.requests
curl -si -H 'X-Request-Id: my-first-trace' localhost:8080/api/status      # look at the header, then the app's log
```
**The real artefact:**
```bash
mvn -q package -pl sb10-production -DskipTests
java -jar sb10-production/target/sb10-production-1.0-SNAPSHOT.jar --spring.profiles.active=prod   # JSON logs
```
**The container** (Docker Desktop running):
```bash
cd sb10-production
docker build -t freightboard .
docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod freightboard
```

## Test Cases
| Request / action | Expected |
|---|---|
| `GET /actuator/health`, anonymous | `{"status":"UP"}`, no `components` |
| `GET /actuator/health` as `admin` | `components.loadBoard.details.openLoads` is a number |
| `GET /actuator/metrics` as `shipper` | `403` |
| `GET /actuator/env` as `admin` | `404`: not exposed |
| A bid placed inside a transaction that rolls back | `freightboard.bids.placed` is unchanged |
| `X-Request-Id: trace-me-42` on a bid request | `[trace-me-42]` on that request's log line |

The full run: **251 tests, 0 failures** (3 PostgreSQL tests skipped without Docker; includes five predictions).

## Definition of Done
- [ ] 251/251 green
- [ ] The jar runs with the `prod` profile and logs JSON
- [ ] The Docker image builds and runs, and `docker exec <container> whoami` prints `freightboard`, not `root`
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: change `@TransactionalEventListener` to `@EventListener` in `BidMetrics`. Which test fails, and why? Put it back.
- [ ] Experiment: remove the `finally` in `RequestIdFilter` (just call `MDC.remove` after `doFilter`). Which test still passes, and in what situation would the id leak anyway? (Hint: what if the controller throws?)
- [ ] Written note (interview-style): "How would you know whether your service is healthy in production? What would you monitor?"
- [ ] Written note: "Why must a health check not depend on business conditions such as 'no loads posted today'?"
- [ ] **Retrospective:** look back at SB01's `StatusController` and today's `BidService`. List five things you'd now do differently in a new Spring Boot project, and one thing you'd like to learn next.

Stuck? See [HINTS.md](HINTS.md).
