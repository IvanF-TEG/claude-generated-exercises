# SB02 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODOs 1–2: pricing strategies
<details><summary>Hint 1 (syntax)</summary>

`Math.max(0, weightKg - 1000)` gives the extra kilograms (never negative). `Math.max(MINIMUM, price)` applies a minimum charge.
</details>
<details><summary>Hint 2 (approach)</summary>

Watch for `int` overflow in bigger calculations. `95L * distanceKm` makes the multiplication `long`. The weight bands work like SB01's vehicle rules: an `if / else if` chain.
</details>
<details><summary>Hint 3 (code)</summary>

```java
long extraKg = Math.max(0, request.weightKg() - 1000);
return 2500 + 95L * request.distanceKm() + 3 * extraKg;
```
</details>

## TODO 3: `QuoteService`
<details><summary>Hint 1 (syntax)</summary>

`List.copyOf(strategies)` for the defensive copy. `Comparator.comparingLong(Quote::pricePence).thenComparing(Quote::strategy)`. `list.getFirst()` (Java 21).
</details>
<details><summary>Hint 2 (approach)</summary>

In `quoteAll`, call `clock.instant()` **once** before the stream, and reuse it, so every quote has the same timestamp. `quoteWith` is `filter` → `findFirst` → `map`: `findFirst` already returns an `Optional`, and `Optional.map` keeps it empty if nothing matched.
</details>
<details><summary>Hint 3 (code)</summary>

```java
var now = clock.instant();
return strategies.stream()
        .map(s -> new Quote(s.name(), s.pricePence(request), now))
        .sorted(Comparator.comparingLong(Quote::pricePence).thenComparing(Quote::strategy))
        .toList();
```
</details>

## TODO 4: manual wiring
<details><summary>Hint 1 (syntax)</summary>

`Clock.systemUTC()` gives the real clock.
</details>
<details><summary>Hint 2 (approach)</summary>

Build the leaves first (the strategies and the clock), then the object that needs them. Spring does the same thing: it works out the order from the constructor parameters.
</details>
<details><summary>Hint 3 (code)</summary>

```java
return new QuoteService(List.of(new DistancePricing(), new WeightBandPricing()), Clock.systemUTC());
```
</details>

## TODO 5: Spring beans
<details><summary>Hint 1 (syntax)</summary>

`@Component` (from `org.springframework.stereotype`) on the two strategies, `@Service` on `QuoteService`. On `PricingConfig`: `@Configuration` on the class and `@Bean` on a method that returns a `Clock`.
</details>
<details><summary>Hint 2 (approach)</summary>

`QuoteService` has a single constructor, so Spring uses it automatically; you don't need `@Autowired`. For the `List<PricingStrategy>` parameter, Spring collects **every** bean that implements the interface.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@Configuration
public class PricingConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
```
</details>

## TODO 6: `QuoteController`
<details><summary>Hint 1 (syntax)</summary>

`ResponseEntity.of(optional)` returns 200 with the body if the `Optional` has a value, or 404 if it's empty.
</details>
<details><summary>Hint 2 (approach)</summary>

Add a `private final QuoteService quoteService;` field and a constructor that takes it, exactly like `QuoteService` takes its dependencies. Is `/api/quotes/cheapest` a clash with `/api/quotes/{strategy}`? No: Spring prefers the more specific, literal path.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@PostMapping("/api/quotes/{strategy}")
public ResponseEntity<Quote> one(@PathVariable String strategy, @RequestBody QuoteRequest request) {
    return ResponseEntity.of(quoteService.quoteWith(strategy, request));
}
```
</details>
