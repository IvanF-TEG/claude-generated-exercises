# SB03 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: binding
<details><summary>Hint 1 (syntax)</summary>

`@ConfigurationProperties("freightboard.pricing")` on the record, and `@EnableConfigurationProperties(PricingProperties.class)` on `PricingConfig`.
</details>
<details><summary>Hint 2 (approach)</summary>

The YAML shape must mirror the record shape: `distance` → `Distance`, `weight-band` → `weightBand`, and `bands` is a YAML list of objects with `max-kg` and `pence-per-km`. If `PricingBindingTest` shows zeros or nulls, compare your indentation with the example in the README.
</details>
<details><summary>Hint 3 (code)</summary>

```yaml
freightboard:
  pricing:
    distance:
      base-fee-pence: 2500
      pence-per-km: 95
      free-weight-kg: 1000
      pence-per-extra-kg: 3
    weight-band:
      minimum-pence: 4000
      heavy-pence-per-km: 160
      bands:
        - max-kg: 1000
          pence-per-km: 60
        - max-kg: 18000
          pence-per-km: 110
```
</details>

## TODO 2: fail fast
<details><summary>Hint 1 (syntax)</summary>

A compact constructor has no parameter list: `public WeightBand { ... }`. You can reassign a parameter inside it (`bands = List.copyOf(bands);`), and the new value is what gets stored.
</details>
<details><summary>Hint 2 (approach)</summary>

Check the list is non-null and non-empty *before* the loop. Then compare each band with the one before it, starting at index 1. "Strictly ascending" means `<=` is an error.
</details>
<details><summary>Hint 3 (code)</summary>

```java
for (int i = 1; i < bands.size(); i++) {
    if (bands.get(i).maxKg() <= bands.get(i - 1).maxKg()) {
        throw new IllegalArgumentException("weight bands must be in ascending order of max-kg");
    }
}
```
</details>

## TODO 3: use the properties
<details><summary>Hint 1 (syntax)</summary>

Store `properties.distance()` (or `properties.weightBand()`) in the field, then use `rates.pencePerKm()` and so on.
</details>
<details><summary>Hint 2 (approach)</summary>

For weight bands: stream the bands, keep the first one where `weightKg <= band.maxKg()`, take its `pencePerKm`, and fall back to `heavyPencePerKm` if none matched. That's `filter` → `findFirst` → `map` → `orElse`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
long perKm = rates.bands().stream()
        .filter(band -> request.weightKg() <= band.maxKg())
        .findFirst()
        .map(PricingProperties.Band::pencePerKm)
        .orElse(rates.heavyPencePerKm());
return Math.max(rates.minimumPence(), perKm * request.distanceKm());
```
</details>

## TODO 4: prod profile
<details><summary>Hint 1 (syntax)</summary>

The file is named `application-prod.yml`, and the name after the dash *is* the profile name.
</details>
<details><summary>Hint 2 (approach)</summary>

Only write the three values that change. Everything else is inherited from `application.yml`. You need the full path to each value, though: the same nesting as in `application.yml`.
</details>
<details><summary>Hint 3 (code)</summary>

```yaml
freightboard:
  pricing:
    distance:
      base-fee-pence: 3000
      pence-per-km: 110
  promo:
    discount-percent: 5
```
</details>

## TODO 5: `PromoPricing`
<details><summary>Hint 1 (syntax)</summary>

`@Component` and `@Profile("promo")` on the class. On the constructor parameter: `@Value("${freightboard.promo.discount-percent:10}") int discountPercent`.
</details>
<details><summary>Hint 2 (approach)</summary>

Ask for `DistancePricing` (the concrete class), not `PricingStrategy`. There are several `PricingStrategy` beans, so asking for the interface would be ambiguous. Multiply before dividing, so integer division rounds down at the end: `price * (100 - pct) / 100`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
public PromoPricing(DistancePricing distancePricing,
                    @Value("${freightboard.promo.discount-percent:10}") int discountPercent) {
    this.distancePricing = distancePricing;
    this.discountPercent = discountPercent;
}
```
</details>

## TODO 6: active profiles
<details><summary>Hint 1 (syntax)</summary>

`org.springframework.core.env.Environment` is itself a bean, so you can inject it like any other. It has `getActiveProfiles()` and `getDefaultProfiles()`, and both return `String[]`.
</details>
<details><summary>Hint 2 (approach)</summary>

If the active array is empty, use the default profiles instead. `List.of(array)` turns an array into a list.
</details>
<details><summary>Hint 3 (code)</summary>

```java
String[] active = environment.getActiveProfiles();
List<String> profiles = List.of(active.length > 0 ? active : environment.getDefaultProfiles());
```
</details>
