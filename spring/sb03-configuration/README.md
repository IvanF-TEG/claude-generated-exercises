# SB03: Configuration and Profiles

**Time box:** 75–90 min · **New concept:** `application.yml`, `@ConfigurationProperties` records, `@EnableConfigurationProperties`, `@Value`, profiles (`application-{profile}.yml`, `@Profile`, `@ActiveProfiles`), property precedence, fail-fast config validation
**Review:** records with compact constructors (Ex 19), `Optional` and streams (Ex 17–18), SB02 beans and injection

## Learning Objective
Move every pricing number out of the Java code and into **configuration**, so prod can charge different rates from dev without a code change or a rebuild. Add a "promo" strategy that only exists when a profile is switched on. Make the app **refuse to start** if the configuration doesn't make sense.

## Primer: where settings come from
**YAML in two minutes.** Indentation is structure (spaces only, never tabs). A `-` starts a list item.
```yaml
freightboard:            # freightboard.pricing.distance.pence-per-km = 95
  pricing:
    distance:
      pence-per-km: 95
    weight-band:
      bands:             # a list of two objects
        - max-kg: 1000
          pence-per-km: 60
        - max-kg: 18000
          pence-per-km: 110
```
`application.properties` says the same thing with one flat line per value: `freightboard.pricing.distance.pence-per-km=95`. Pick one format per project. From now on, FreightBoard uses YAML.

| Python | Spring Boot |
|---|---|
| `os.environ.get("RATE", "95")` | `@Value("${freightboard.rate:95}") long rate` |
| A settings object from `pydantic-settings` | A `@ConfigurationProperties("freightboard.pricing")` **record** |
| `settings.dev.py` / `settings.prod.py` | `application.yml` + `application-prod.yml`, chosen with a **profile** |

**`@ConfigurationProperties` vs `@Value`**
| | `@ConfigurationProperties` record | `@Value("${...}")` |
|---|---|---|
| Best for | A *group* of related settings | One single value |
| Type-safe, with nested objects and lists | ✅ | ❌ (single values only) |
| Relaxed binding (`pence-per-km` = `pencePerKm` = `PENCE_PER_KM`) | ✅ | Partial |
| Easy to unit test | ✅ Just `new` the record | ❌ Needs Spring |

**Profiles** are named sets of configuration. When `prod` is active, Spring loads `application.yml` **and then** `application-prod.yml` on top, so prod only lists what's *different*. A class annotated `@Profile("promo")` only becomes a bean when `promo` is active.

How to switch a profile on:
```bash
mvn -q spring-boot:run -pl sb03-configuration -Dspring-boot.run.profiles=prod    # Maven plugin
java -jar app.jar --spring.profiles.active=prod                                   # a packaged app (SB10)
SPRING_PROFILES_ACTIVE=prod java -jar app.jar                                      # environment variable (typical in Docker/Kubernetes)
```
In tests, use `@ActiveProfiles("prod")`.

**Precedence (the short version).** When the same property is set in more than one place, the source *higher* in this list wins:
1. Test properties: `@SpringBootTest(properties = ...)`
2. Command-line arguments: `--freightboard.pricing.distance.pence-per-km=150`
3. Environment variables: `FREIGHTBOARD_PRICING_DISTANCE_PENCEPERKM=150`
4. `application-{profile}.yml`. With several profiles active, the **last** one listed wins
5. `application.yml`
6. The default in `@Value("${name:default}")`

**Fail fast.** A typo in config should stop the app at start-up, not quietly charge a customer the wrong price three weeks later. A compact constructor on a properties record is a simple, plain-Java way to do this. (In SB05 you'll meet `@Validated`, which does the same with annotations.)

> ⚠️ **Secrets don't belong in `application.yml`.** It's committed to Git. Passwords and API keys come from environment variables or a secrets manager. You'll see this again in SB08 (database passwords) and SB09 (users).

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `PricingProperties.java`, `PricingConfig.java`, `application.yml` | Bind `freightboard.pricing.*` to the record, and write the default values in YAML |
| 2 | `PricingProperties.java` | Compact constructors that reject bad configuration |
| 3 | `DistancePricing.java`, `WeightBandPricing.java` | Replace the hard-coded numbers with the injected properties |
| 4 | `application-prod.yml` | Prod rates |
| 5 | `PromoPricing.java` | A strategy that only exists in the `promo` profile, with `@Value` and a default |
| 6 | `status/StatusController.java` | Report the active profiles in `GET /api/status` |
| Predictions | `src/test/java/com/freightboard/quotes/PredictionsTest.java` | Which value wins? Replace every `-1` *after* TODOs 1–5 but *before* running |

> ⚠️ **Do TODO 1 first.** The strategies already ask for a `PricingProperties` in their constructors, so until TODO 1 is done, Spring can't create them and **every Spring test errors at start-up**. That includes SB01's and SB02's tests. The error message ("Parameter 0 of constructor in ...DistancePricing required a bean of type ...PricingProperties that could not be found") is worth reading: you'll meet it again at work.

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb03-configuration
mvn -q spring-boot:run -pl sb03-configuration -Dspring-boot.run.profiles=prod,promo
curl -s localhost:8080/api/status
curl -s -X POST localhost:8080/api/quotes -H 'Content-Type: application/json' \
     -d '{"origin":"LS1","destination":"M1","distanceKm":70,"weightKg":1500}'
```
In IntelliJ: **Run → Edit Configurations → FreightBoardApplication → Active profiles**.

## Test Cases
| Profiles | `distance` quote for 70 km, 1500 kg | `promo` quote |
|---|---|---|
| none | 10650p | no promo strategy |
| `prod` | 12200p (3000 + 110 × 70 + 3 × 500) | no promo strategy |
| `promo` | 10650p | 8520p (20% off) |
| `@SpringBootTest(properties = "...pence-per-km=200")` | 18000p | no promo strategy |

`GET /api/status` with `prod` active returns `{"service":"FreightBoard","status":"UP","activeProfiles":["prod"]}`.

The full run: **86 tests, 0 failures** (including seven predictions).

## Definition of Done
- [ ] 86/86 green
- [ ] The app runs with `prod,promo`, and the `curl` output matches the table
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `application.yml`, swap the order of the two weight bands and start the app. Read the error: which property failed, and does it show *your* message? Put the order back.
- [ ] Experiment: indent one line in `application.yml` with a tab instead of spaces. What happens?
- [ ] Written note (interview-style): "How would you give the same jar different settings in test and production?" Mention profiles, environment variables and precedence.
- [ ] Written note: "When would you choose `@ConfigurationProperties` over `@Value`?"

Stuck? See [HINTS.md](HINTS.md).
