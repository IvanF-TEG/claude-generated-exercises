# SB10 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: Actuator configuration
<details><summary>Hint 1 (syntax)</summary>

`management.endpoints.web.exposure.include: health, info, metrics`, `management.endpoint.health.show-details: when-authorized`, `management.endpoint.health.roles: ADMIN`, `management.info.env.enabled: true`, and then an `info:` block of your own.
</details>
<details><summary>Hint 2 (approach)</summary>

`management` and `info` are **top-level** YAML keys, not under `spring` or `freightboard`. In `SecurityConfig`, the actuator rules go *before* `anyRequest()`. Include `/actuator/health/**` so the health sub-paths are public too.
</details>
<details><summary>Hint 3 (code)</summary>

```java
.requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
.requestMatchers("/actuator/**").hasRole(ADMIN)
```
</details>

## TODO 2: health indicator
<details><summary>Hint 1 (syntax)</summary>

`Health.up().withDetail("openLoads", count).build()` and `Health.down(e).build()`.
</details>
<details><summary>Hint 2 (approach)</summary>

Wrap the repository call in `try/catch (Exception e)`. `Health.down(e)` records the exception as the `error` detail.
</details>
<details><summary>Hint 3 (code)</summary>

```java
try {
    return Health.up().withDetail("openLoads", loadRepository.countByStatus(LoadStatus.OPEN)).build();
} catch (Exception e) {
    return Health.down(e).build();
}
```
</details>

## TODO 3: events and metrics
<details><summary>Hint 1 (syntax)</summary>

`events.publishEvent(new BidPlaced(saved.getId(), loadId, carrier.getId(), saved.getAmountPence()));` and `@TransactionalEventListener` (from `org.springframework.transaction.event`).
</details>
<details><summary>Hint 2 (approach)</summary>

In `acceptBid`, collect the rejected bids into a list first (`.toList()`), then reject them, so you know how many there were for the event. The listener methods only need `counter.increment()`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
List<Bid> rejected = load.getBids().stream()
        .filter(other -> other != bid && other.getStatus() == BidStatus.PENDING)
        .toList();
rejected.forEach(Bid::reject);
load.book();
events.publishEvent(new BidAccepted(bidId, load.getId(), rejected.size()));
```
</details>

## TODO 4: logging
<details><summary>Hint 1 (syntax)</summary>

`log.info("Bid {} placed on load {} by carrier {} for {}p", id, loadId, carrierId, amount);`
</details>
<details><summary>Hint 2 (approach)</summary>

Match the message text in the TODO exactly: the test searches for it. Use the carrier's **id**, never its name or username. For prod, `logging.structured.format.console: ecs` goes in `application-prod.yml`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
log.info("Bid {} accepted on load {}, {} other bid(s) rejected", bidId, load.getId(), rejected.size());
```
</details>

## TODO 5: request ids
<details><summary>Hint 1 (syntax)</summary>

`request.getHeader(HEADER)`, `UUID.randomUUID().toString()`, `MDC.put(MDC_KEY, id)`, `response.setHeader(HEADER, id)`, `MDC.remove(MDC_KEY)`.
</details>
<details><summary>Hint 2 (approach)</summary>

Set the response header *before* `chain.doFilter`: once the response starts being written, headers can't be added any more. The `try/finally` goes around `chain.doFilter`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
MDC.put(MDC_KEY, requestId);
response.setHeader(HEADER, requestId);
try {
    chain.doFilter(request, response);
} finally {
    MDC.remove(MDC_KEY);
}
```
</details>

## TODO 6: Dockerfile
<details><summary>Hint 1 (syntax)</summary>

`FROM image AS builder` names a stage. `COPY --from=builder /path ./` copies from it.
</details>
<details><summary>Hint 2 (approach)</summary>

Build the jar with Maven *first*: the Dockerfile only copies `target/*.jar`. Copy the layers from least to most often changed, so Docker can reuse the cached ones.
</details>
<details><summary>Hint 3 (code)</summary>

```dockerfile
FROM eclipse-temurin:21-jre AS builder
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --create-home freightboard
USER freightboard
COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
```
</details>
