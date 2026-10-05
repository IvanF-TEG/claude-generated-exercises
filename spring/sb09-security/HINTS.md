# SB09 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1a: URL rules
<details><summary>Hint 1 (syntax)</summary>

`.requestMatchers(HttpMethod.POST, "/api/loads", "/api/loads/*/cancel").hasRole(SHIPPER)`, `.requestMatchers("/api/status").permitAll()` (no method = any method), `.hasAnyRole(SHIPPER, ADMIN)`.
</details>
<details><summary>Hint 2 (approach)</summary>

First match wins, so the public paths go first, then the specific method+path rules, and `anyRequest().authenticated()` stays last. `GET /api/loads/*/bids` must come before the catch-all, or carriers could read it. `hasRole("CARRIER")` checks for the authority `ROLE_CARRIER`: `roles(...)` on a user adds the `ROLE_` prefix for you.
</details>
<details><summary>Hint 3 (code)</summary>

```java
.requestMatchers(HttpMethod.POST, "/api/loads/*/bids").hasRole(CARRIER)
.requestMatchers(HttpMethod.GET, "/api/loads/*/bids").hasAnyRole(SHIPPER, ADMIN)
.requestMatchers(HttpMethod.PUT, "/api/loads/*").hasRole(SHIPPER)
.requestMatchers(HttpMethod.DELETE, "/api/loads/*").hasRole(ADMIN)
```
</details>

## TODO 1b: method security
<details><summary>Hint 1 (syntax)</summary>

`@EnableMethodSecurity` on `SecurityConfig`, and `@PreAuthorize("hasRole('ADMIN')")` on `LoadService.delete`.
</details>
<details><summary>Hint 2 (approach)</summary>

Like `@Transactional`, `@PreAuthorize` works through a proxy. It checks the role *before* the method body runs.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public void delete(long id) { ... }
```
</details>

## TODO 2: encoder and users
<details><summary>Hint 1 (syntax)</summary>

`PasswordEncoderFactories.createDelegatingPasswordEncoder()`. `User.withUsername("shipper").password(hash).roles(SHIPPER).build()`.
</details>
<details><summary>Hint 2 (approach)</summary>

Encode the password *once* and reuse the hash for all five users (bcrypt is slow on purpose). Pass all five `UserDetails` to `new InMemoryUserDetailsManager(...)`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
String hash = encoder.encode(password);
return new InMemoryUserDetailsManager(
        User.withUsername("shipper").password(hash).roles(SHIPPER).build(),
        ...);
```
</details>

## TODO 3: `V4__add_owners.sql`
<details><summary>Hint 1 (syntax)</summary>

The same add → backfill → `SET NOT NULL` pattern as V3, for `loads.shipper`. For `carriers.username`: `ADD COLUMN` then `ADD CONSTRAINT uk_carriers_username UNIQUE (username)`, with no `NOT NULL`.
</details>
<details><summary>Hint 2 (approach)</summary>

Every existing load gets `'system'`. Existing carriers keep `NULL`: nobody can bid for them until an admin links a login.
</details>
<details><summary>Hint 3 (code)</summary>

```sql
ALTER TABLE loads ADD COLUMN shipper VARCHAR(50);
UPDATE loads SET shipper = 'system';
ALTER TABLE loads ALTER COLUMN shipper SET NOT NULL;
```
</details>

## TODOs 4–5: identity and ownership
<details><summary>Hint 1 (syntax)</summary>

`carrierService.forUser(username)` is given. `throw new AccessDeniedException("Load " + id + " belongs to another shipper")` (from `org.springframework.security.access`).
</details>
<details><summary>Hint 2 (approach)</summary>

Write `private Load ownedOpenLoad(long id, String username)`: `get(id)` (404), then the owner check (403), then the existing `openLoad` state check (409). Owner *before* state, so a stranger can't find out that someone else's load is cancelled. In `acceptBid`, check `bid.getLoad().getShipper()` straight after finding the bid.
</details>
<details><summary>Hint 3 (code)</summary>

```java
private Load ownedOpenLoad(long id, String username) {
    Load load = get(id);
    if (!load.getShipper().equals(username)) {
        throw new AccessDeniedException("Load " + id + " belongs to another shipper");
    }
    return openLoad(id);
}
```
</details>

## TODO 6: controllers
<details><summary>Hint 1 (syntax)</summary>

Add `Principal principal` (from `java.security`) as a handler parameter, and call `principal.getName()`.
</details>
<details><summary>Hint 2 (approach)</summary>

Spring fills it in from the security context. Tests with `@WithMockUser(username = "pennine")` or `.with(user("pennine"))` set that name.
</details>
<details><summary>Hint 3 (code)</summary>

```java
public LoadResponse cancel(@PathVariable long id, Principal principal) {
    return LoadResponse.from(loadService.cancel(id, principal.getName()));
}
```
</details>
