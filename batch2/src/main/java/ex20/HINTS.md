# Exercise 20: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- A switch expression that returns a value ends with `};`, and each arm is `case X -> value;`. For an arm that needs several statements, use a block with `yield`: `case "AT_DEPOT" -> { ...; yield new ArrivedAtDepot(...); }`
- Throwing from an arm is allowed: `default -> throw new IllegalArgumentException("...");`
- Record patterns list **every** component, in declaration order. Use `var` to let the compiler infer the types: `case OutForDelivery(var id, var hour, var driver) -> ...`
- `instanceof` with a record pattern: `if (e instanceof DeliveryFailed(var id, var hour, var reason)) { ... reason ... }`
- `line.split(",", -1)` keeps a trailing empty field, so `"P-300,50,DELIVERED,"` gives 4 parts.
- `new EnumMap<>(Status.class)`: an `EnumMap` needs to be told which enum it's for.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **describe:** put the two `Delivered` cases next to each other, the guarded one *first*.
- **statusAfter:** one `case Type t when <current is allowed> -> NEW_STATUS;` per event type, then a single `default -> throw new InvalidTransitionException(current, event);`. For ArrivedAtDepot, the guard has three `||` alternatives.
- **replay:** `events.stream().sorted(Comparator.comparingInt(TrackingEvent::hour)).toList()` gives you a sorted *copy*. Stream sorting is stable, so events with the same hour keep their order. Then loop, and for each event: `map.put(id, statusAfter(map.get(id), event))`. `map.get` returns `null` for a new parcel, which is exactly what `statusAfter` expects.
- **slaBreaches:** build two maps first, parcel id → pickup hour and parcel id → delivered hour (`filter(e -> e instanceof PickedUp)`, then `toMap`). Then stream the pickup map's keys. `delivered.getOrDefault(id, nowHour)` handles "not delivered yet" neatly.
- **failureReasons:** turn each event into a stream of zero or one reasons with `flatMap`: `e instanceof DeliveryFailed(...) ? Stream.of(reason) : Stream.empty()`. Then use the same `groupingBy(..., TreeMap::new, counting())` as in Ex 18.
- **byStatus:** stream `statuses.entrySet()`, and group by value into an `EnumMap`, mapping each entry to its key.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// describe: the shape (fill in the rest)
return switch (event) {
    case PickedUp(var id, var hour, var customer) -> id + " collected from " + customer + " (hour " + hour + ")";
    case ArrivedAtDepot(var id, var hour, Depot(var code, var region)) -> /* TODO */;
    // TODO: OutForDelivery
    case Delivered(var id, var hour, var signedBy) when signedBy.isBlank() -> /* TODO */;
    case Delivered(var id, var hour, var signedBy) -> /* TODO */;
    // TODO: DeliveryFailed
};

// statusAfter: the first two arms
return switch (event) {
    case PickedUp p when current == null -> Status.COLLECTED;
    case ArrivedAtDepot a when current == Status.COLLECTED || current == Status.AT_DEPOT || current == Status.FAILED -> Status.AT_DEPOT;
    // TODO: three more
    default -> throw new InvalidTransitionException(current, event);
};

// byStatus: groupingBy with a map factory that is a Supplier (a lambda with no parameters)
return statuses.entrySet().stream()
        .collect(groupingBy(Map.Entry::getValue, () -> new EnumMap<>(Status.class), /* TODO: downstream */));
```
</details>
