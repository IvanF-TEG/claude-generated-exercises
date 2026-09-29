# Exercise 15: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Enums already implement `Comparable`: `priority.compareTo(other.priority)` compares by declaration order.
- `String` implements `Comparable` too: `id.compareTo(other.id)`.
- Copy-then-modify: `Set<String> result = new TreeSet<>(a); result.retainAll(b);`
- Most collections have a "copy constructor": `new PriorityQueue<>(jobs)`, `new ArrayList<>(jobs)`, `new LinkedHashSet<>(scans)`.
- `ArrayDeque` as a queue: `offer(x)` adds at the back, `poll()` removes from the front (and returns `null` when empty).
- `ArrayDeque` as a stack: `push(x)` and `pop()` both work at the front.
- Explicit iterator: `Iterator<Consignment> it = jobs.iterator(); while (it.hasNext()) { Consignment c = it.next(); ... it.remove(); }`
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **compareTo / compare with several keys:** compare the first key. If the result isn't 0, return it. Otherwise move on to the next key. For "heaviest first", swap the arguments: `Integer.compare(b.getWeightKg(), a.getWeightKg())`.
- **uniqueScans** is one line. **duplicateScans** needs two sets: one for "seen so far" and a `LinkedHashSet` for the answer. `seen.add(id)` returns `false` if `id` was already there.
- **dispatchOrder:** keep polling while the result has fewer than `slots` items *and* the queue isn't empty.
- **serveDock:** `event.startsWith("ARRIVE ")` tells you which kind of event it is, and `event.substring(7)` gives the truck's name. If `poll()` returns `null`, record `"IDLE"`.
- **loadingOrder:** push every consignment in drop order, then pop until the stack is empty.
- **removeOverweight:** calling `jobs.remove(c)` inside a for-each loop changes the collection behind the loop's back. `iterator.remove()` is the one safe way to remove while iterating.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// Comparing by several keys (TODO 1 has one more key than this)
int byPriority = priority.compareTo(other.priority);
if (byPriority != 0) {
    return byPriority;
}
return Integer.compare(deadlineHour, other.deadlineHour);

// The shape of an anonymous Comparator (TODO 6)
return new Comparator<Consignment>() {
    @Override
    public int compare(Consignment a, Consignment b) {
        // same multi-key pattern as above, using postcode then id
    }
};

// Draining a PriorityQueue (TODO 5)
PriorityQueue<Consignment> queue = new PriorityQueue<>(jobs);
List<Consignment> chosen = new ArrayList<>();
while (chosen.size() < slots && !queue.isEmpty()) {
    chosen.add(queue.poll());
}
```
</details>
