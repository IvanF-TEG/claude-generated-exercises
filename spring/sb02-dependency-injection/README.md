# SB02: Beans and Dependency Injection

**Time box:** 75–90 min · **New concept:** inversion of control, constructor injection, `@Component`/`@Service`, `@Configuration` + `@Bean`, injecting a `List` of beans, `@Primary`, singleton scope
**Review:** interfaces and the strategy pattern (Ex 13), streams and `Comparator` (Ex 16–17), `Optional` (Ex 18), SB01 controllers

## Learning Objective
Add freight **quotes** to FreightBoard: several pricing strategies each give a price, and the service returns them cheapest first. You'll wire the objects together **by hand** first, then let Spring do it. By the end you should be able to explain what the Spring container is, why constructor injection makes code easy to test, and what happens when Spring finds two beans that could fit.

## Primer: dependency injection
**The problem.** A class that creates its own dependencies is welded to them:
```java
public class QuoteService {
    private final List<PricingStrategy> strategies = List.of(new DistancePricing(), new WeightBandPricing());
    // How do you test this with a fake strategy? Or a fixed clock? You can't, without changing the class.
}
```
**The fix: dependency injection (DI).** The class *asks* for what it needs in its constructor, and something *outside* decides what to pass in:
```java
public QuoteService(List<PricingStrategy> strategies, Clock clock) { ... }
```
In a test, *you* pass fakes. In the running app, *Spring* passes the real beans. This is called **inversion of control**: your class no longer controls how its dependencies are created.

| Python | Java / Spring |
|---|---|
| Pass collaborators into `__init__` | Constructor parameters |
| FastAPI's `Depends(get_service)` | Spring sees a constructor parameter and injects a matching bean |
| A module-level singleton | A Spring bean (singleton by default: one shared instance) |

**Vocabulary**
| Term | Meaning |
|---|---|
| **Bean** | An object that Spring creates and manages |
| **ApplicationContext** (the container) | Spring's registry of every bean: it creates them, injects them into each other, and shuts them down |
| `@Component` | "Create one of these and manage it." `@Service`, `@Repository` and `@RestController` are specialised versions that also signal the class's role |
| `@Configuration` + `@Bean` | A method whose return value becomes a bean. Use it for classes you can't annotate, such as `java.time.Clock` |
| **Injection point** | A constructor parameter (or field) that Spring must fill |

**How Spring chooses a bean for an injection point**
| Beans of that type | `Greeter greeter` (one wanted) | `List<Greeter> greeters` (all wanted) |
|---|---|---|
| 0 | start-up fails | you predict it in `PredictionsTest` |
| 1 | that bean | a list of one |
| 2+ | start-up fails, **unless** one is `@Primary` or you use `@Qualifier("name")` | all of them |

**Constructor, field or setter injection?** Always prefer constructor injection. Dependencies can be `final`, the object is never half-built, and tests can call the constructor directly, as `QuoteServiceTest` does. You'll still see `@Autowired private Foo foo;` (field injection) in older code. It works, but it hides dependencies and forces tests to start Spring.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `quotes/DistancePricing.java` | Base fee + per-km + per-kg-over-1000 |
| 2 | `quotes/WeightBandPricing.java` | Rate per km by weight band, with a minimum charge |
| 3 | `quotes/QuoteService.java` | Constructor, `strategyNames`, `quoteAll` (cheapest first), `cheapest`, `quoteWith` |
| 4 | `quotes/ManualWiring.java` | Build the object graph by hand, with `new` only. Then run `main` |
| 5 | three bean classes + `quotes/PricingConfig.java` | Make them Spring beans; add a `Clock` `@Bean` |
| 6 | `quotes/QuoteController.java` | Three `POST` endpoints, with constructor injection |
| Predictions | `src/test/java/com/freightboard/quotes/PredictionsTest.java` | Replace every `-1` / `"???"` *before* running |

**Order matters here:** do TODOs 1–4 with **no Spring annotations at all**, and get `PricingStrategiesTest`, `QuoteServiceTest` and `ManualWiringTest` green. Only then do TODO 5. That's the point of the exercise: the design works without the framework, and the framework only automates the wiring.

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb02-dependency-injection
mvn -q spring-boot:run -pl sb02-dependency-injection
curl -s -X POST localhost:8080/api/quotes -H 'Content-Type: application/json' \
     -d '{"origin":"LS1","destination":"M1","distanceKm":70,"weightKg":1500}'
```
Also run `ManualWiring.main` from IntelliJ (▶ in the margin). It's the same quotes, with no Spring involved.

## Test Cases
| Request (70 km, 1500 kg) | Expected |
|---|---|
| `POST /api/quotes` | `200`, `[{"strategy":"weight-band","pricePence":7700,...},{"strategy":"distance","pricePence":10650,...}]` |
| `POST /api/quotes/cheapest` | `200`, the `weight-band` quote |
| `POST /api/quotes/distance` | `200`, the `distance` quote |
| `POST /api/quotes/teleport` | `404` |

`quotedAt` is an ISO-8601 timestamp such as `"2026-01-05T09:00:00Z"`. In `QuoteControllerTest` it's fixed, because the test adds a `@Primary` `Clock` bean.

The full run: **66 tests, 0 failures** (SB01's tests are still here and must stay green, plus seven predictions).

## Definition of Done
- [ ] 66/66 green
- [ ] `ManualWiring.main` prints two quotes, and the `curl` above gives the same prices
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: remove `@Component` from `WeightBandPricing`. Which tests fail? Does the app still start? Put it back.
- [ ] Experiment: remove `@Bean` from `PricingConfig.clock()`. Read the start-up error message carefully. Which bean couldn't be created, and which parameter was missing? Put it back.
- [ ] Experiment: add a third strategy, `FlatRatePricing` (name `"flat"`, always 9000p), as a `@Component`. Which classes did you have to change to make it appear in `POST /api/quotes`? (Answer: none. That's the open/closed principle.) Delete it afterwards, or update `SpringWiringTest`.
- [ ] Written note (interview-style): "What is dependency injection, and why is constructor injection preferred over field injection?"
- [ ] Written note: "What's the difference between `@Component` and `@Bean`?"

Stuck? See [HINTS.md](HINTS.md).
