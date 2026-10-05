# SB04 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: `InMemoryLoadRepository`
<details><summary>Hint 1 (syntax)</summary>

`nextId.getAndIncrement()` returns the current value and then adds 1, atomically. `Optional.ofNullable(map.get(id))`. `map.remove(id)` returns the removed value, or `null`.
</details>
<details><summary>Hint 2 (approach)</summary>

In `save`: if `load.id() == 0`, make a copy with a new id (`load.withId(...)`). Either way, `put` it into the map and return the version you stored. A `ConcurrentHashMap` has no order, so sort in `findAll`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
Load toStore = load.id() == 0 ? load.withId(nextId.getAndIncrement()) : load;
loads.put(toStore.id(), toStore);
return toStore;
```
</details>

## TODO 2: `LoadService`
<details><summary>Hint 1 (syntax)</summary>

`Optional.map(load -> repository.save(...))` runs only if the load exists, and stays empty otherwise. That makes `update` and `cancel` one line each.
</details>
<details><summary>Hint 2 (approach)</summary>

For `search`, chain two `filter`s, each of the form "filter is null OR the load matches". Write the origin check as `origin.equalsIgnoreCase(load.origin())`, with the non-null parameter first, so a load with a null origin can't cause a `NullPointerException`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
return repository.findAll().stream()
        .filter(load -> status == null || load.status() == status)
        .filter(load -> origin == null || origin.equalsIgnoreCase(load.origin()))
        .toList();
```
</details>

## TODOs 3–6: `LoadController`
<details><summary>Hint 1 (syntax)</summary>

`@RequestMapping("/api/loads")` on the class, then `@GetMapping`, `@GetMapping("/{id}")`, `@PostMapping`, `@PutMapping("/{id}")`, `@PostMapping("/{id}/cancel")` and `@DeleteMapping("/{id}")`. Optional query parameters use `@RequestParam(required = false)`. Spring converts `"OPEN"` to `LoadStatus.OPEN` for you.
</details>
<details><summary>Hint 2 (approach)</summary>

`ResponseEntity.of(optional)` handles 200/404. For `201`, `ResponseEntity.created(uri).body(load)`. Build the URI from the *current request's* URL, so it's correct on any host and port: `ServletUriComponentsBuilder.fromCurrentRequest()`. For `204`, `ResponseEntity.noContent().build()`, with return type `ResponseEntity<Void>`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@PostMapping
public ResponseEntity<Load> create(@RequestBody LoadRequest request) {
    Load created = loadService.create(request);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
    return ResponseEntity.created(location).body(created);
}
```
</details>
