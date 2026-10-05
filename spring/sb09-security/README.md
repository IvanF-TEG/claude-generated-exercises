# SB09: Security (Authentication, Roles and Ownership)

**Time box:** 90–120 min · **New concept:** Spring Security's filter chain, authentication vs authorisation, HTTP Basic, password hashing (bcrypt), `SecurityFilterChain` URL rules, roles, `Principal`, object-level checks (ownership), `@PreAuthorize` method security, 401 vs 403, `@WithMockUser`
**Review:** SB03 secrets in configuration, SB05 exceptions → status codes, SB08 migrations

## Learning Objective
Right now anyone on the internet can cancel any load, and any client can bid *as any carrier* just by sending that carrier's id. You'll add logins, give each user a role, and write the rules for who may call what. Then you'll add the check that most real APIs forget: **may this user touch *this particular* load?**

## Primer
**Two different questions**
| | Authentication (AuthN) | Authorisation (AuthZ) |
|---|---|---|
| Question | *Who are you?* | *Are you allowed to do this?* |
| Fails with | **401 Unauthorized** (a misleading name: it really means "unauthenticated") | **403 Forbidden** |
| In FreightBoard | HTTP Basic: username + password on every request | Roles (URL rules), then ownership (service checks) |

**How it works.** Spring Security is a chain of servlet *filters* that runs **before** any controller. A request that fails a rule never reaches your code, not even to get a 404.

```
request → [ authenticate: who is it? ] → [ URL rules: role allowed? ] → controller → service: [ owner? ] → ...
             401 if unknown/bad password      403 if wrong role                         403 if not theirs
```

| Python (FastAPI / Django) | Spring Security |
|---|---|
| `Depends(get_current_user)` | `Principal principal` (or `Authentication`) as a controller parameter |
| `@permission_required("loads.add")` | `.requestMatchers(POST, "/api/loads").hasRole("SHIPPER")` |
| `passlib` / `make_password` (bcrypt) | `PasswordEncoder` (bcrypt via `DelegatingPasswordEncoder`) |
| Checking `obj.owner == request.user` in the view | Checking `load.getShipper().equals(username)` in the **service** |

**Passwords are never stored.** You store a one-way **hash**. bcrypt is deliberately slow (so guessing is expensive) and salted (so equal passwords give different hashes). `{bcrypt}$2a$10$...`: the `{bcrypt}` prefix names the algorithm, so it can be upgraded later.

**Roles vs ownership.** "Only shippers may cancel loads" is a *role* rule: it goes in `SecurityConfig`. "Only the shipper who posted load 7 may cancel load 7" is an *object* rule: it needs the data, so it goes in the service. Missing object checks are **OWASP's number one API security risk** (Broken Object Level Authorization).

**Never trust the body to say who the caller is.** SB07's `BidRequest` had a `carrierId`, which any client could fill in with any value. Identity comes from the authenticated principal only.

> ⚠️ **In real systems:** the demo users live in memory and share one password from configuration, which is fine for learning only. Production apps keep users in a database, or delegate login to an identity provider (OAuth2 / OpenID Connect with Keycloak, Entra ID, Auth0 and so on) and receive **tokens** instead of passwords. HTTP Basic must only ever travel over **HTTPS**. Usernames are personal data under GDPR, so think about who can see `shipper` in API responses and logs.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1a | `security/SecurityConfig.java` | The URL rules, in the right order |
| 1b | `security/SecurityConfig.java`, `loads/LoadService.java` | `@EnableMethodSecurity` + `@PreAuthorize` on `delete` (defence in depth) |
| 2 | `security/SecurityConfig.java` | A bcrypt `PasswordEncoder` and five demo users |
| 3 | `db/migration/V4__add_owners.sql`, `loads/Load.java` | `loads.shipper` (backfilled) and `carriers.username` (unique) |
| 4 | `bids/BidService.java` | The carrier comes from the login, not the request |
| 5 | `loads/LoadService.java`, `bids/BidService.java` | Only the owner may update/cancel a load or accept its bids |
| 6 | `loads/LoadController.java`, `bids/BidController.java` | Pass `principal.getName()` to the services |
| Predictions | `src/test/java/com/freightboard/security/PredictionsTest.java` | 401 or 403? Cookies? Replace every `-1` / `"???"` *before* running |

Suggested order: **3 → 2a → 2b → 1a** (`SecurityRulesTest`) **→ 1b** (`MethodSecurityTest`) **→ 4 → 5 → 6** (`OwnershipTest`).

> ⚠️ **Do TODO 3 and TODO 2a first.** `Carrier` already maps a `username` column, so without V4, schema validation stops the app. Without a `PasswordEncoder`, no user can log in. Until both are done, nearly every Spring test errors.

**Demo users** (password `freight123`): `shipper` and `shipper2` (SHIPPER), `pennine` and `dales` (CARRIER), `admin` (ADMIN).

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb09-security
mvn -q spring-boot:run -pl sb09-security
```
Then:
```bash
curl -i localhost:8080/api/loads                                        # 401 and a WWW-Authenticate header
curl -s -u shipper:freight123 localhost:8080/api/loads                  # -u sends HTTP Basic credentials
curl -s -u admin:freight123 -X POST localhost:8080/api/carriers -H 'Content-Type: application/json' \
     -d '{"name":"Pennine Haulage","maxWeightKg":20000,"username":"pennine"}'
curl -s -u shipper:freight123 -X POST localhost:8080/api/loads -H 'Content-Type: application/json' \
     -d '{"origin":"LS1","destination":"M1","weightKg":1500,"pickupDate":"2031-03-01"}'
curl -s -u pennine:freight123 -X POST localhost:8080/api/loads/1/bids -H 'Content-Type: application/json' -d '{"amountPence":9000}'
curl -i -u shipper2:freight123 -X POST localhost:8080/api/bids/1/accept   # 403: not shipper2's load
curl -s -u shipper:freight123 -X POST localhost:8080/api/bids/1/accept
```

## Test Cases
| Request | Expected |
|---|---|
| `GET /api/loads/999`, not logged in | `401` (security runs before the controller, so it's not a 404) |
| `POST /api/loads` as `pennine` (CARRIER) | `403` |
| `GET /api/loads/7/bids` as `pennine` | `403`: carriers can't see rivals' prices |
| `POST /api/loads/7/cancel` as `shipper2` on `shipper`'s load | `403` |
| `loadService.delete(...)` called directly as a SHIPPER | `AccessDeniedException` |

The full run: **232 tests, 0 failures** (3 PostgreSQL tests skipped without Docker; includes six predictions).

## Definition of Done
- [ ] 232/232 green
- [ ] The `curl` session above behaves as described
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: move the `.anyRequest().authenticated()` line to the *top* of the rules. What happens to `GET /api/status`, and why? Put it back.
- [ ] Experiment: in `SecurityRulesTest`, add a row proving a CARRIER can't `DELETE` a load. Did you need to change `SecurityConfig`?
- [ ] Written note (interview-style): "What's the difference between authentication and authorisation? Between 401 and 403?"
- [ ] Written note: "What is Broken Object Level Authorization, and where in FreightBoard did you prevent it?"
- [ ] Written note: "Why did we disable CSRF protection, and when would that be a mistake?"

Stuck? See [HINTS.md](HINTS.md).
