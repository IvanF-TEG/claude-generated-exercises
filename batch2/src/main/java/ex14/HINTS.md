# Exercise 14: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Field: `private final Map<String, Delivery> byId = new LinkedHashMap<>();`
- `putIfAbsent(k, v)` adds only if the key is missing, and returns `null` when it added something.
- Iterating values in insertion order: `for (Delivery d : byId.values()) { ... }`
- Grouping: `groups.computeIfAbsent(d.getArea(), area -> new ArrayList<>()).add(d);`
- Sorting a list in place: `Collections.sort(list);`, or `list.sort(null)` for natural order.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **record:** either `if (byId.containsKey(id)) return false;` and then `put`, or a single `putIfAbsent`. Never plain `put`, which would *replace* the original.
- **parcelsFor:** you can build on `parcelsPerDriver()` rather than looping again.
- **busiestArea:** count parcels into a **`TreeMap`**, so `entrySet()` comes out alphabetically. Then keep the best entry, replacing it only when a count is **strictly** greater. The tie-break then comes free.
- **onTimePercentByDriver:** make one `LinkedHashMap` of deliveries per driver (it gives you the first-seen order) and a second map of on-time deliveries per driver. Then build the result by walking the first map: `onTime * 100 / total` with `int` division rounds down. Watch out for drivers with *no* on-time deliveries (`getOrDefault`).
- **invert:** for each `entry` of the input, the *value* (the van) becomes the key: `computeIfAbsent(entry.getValue(), ...).add(entry.getKey())`. Sort every list at the end.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// busiestArea
Map<String, Integer> parcelsByArea = new TreeMap<>();
for (Delivery d : byId.values()) {
    parcelsByArea.merge(d.getArea(), d.getParcels(), Integer::sum);
}
String busiest = null;
int most = 0;
for (Map.Entry<String, Integer> entry : parcelsByArea.entrySet()) {
    // TODO: replace busiest/most when this entry is strictly bigger
}
return busiest;

// onTimePercentByDriver: the final loop
Map<String, Integer> percent = new LinkedHashMap<>();
for (Map.Entry<String, Integer> entry : totalByDriver.entrySet()) {
    int onTime = onTimeByDriver.getOrDefault(entry.getKey(), 0);
    percent.put(entry.getKey(), onTime * 100 / entry.getValue());
}
```
</details>
