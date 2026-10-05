# SB07 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: mapping relationships
<details><summary>Hint 1 (syntax)</summary>

`@ManyToOne(fetch = FetchType.LAZY, optional = false)` and `@JoinColumn(name = "load_id")` on `Bid.load` (the same pattern for `carrier`). On `Load.bids`: `@OneToMany(mappedBy = "load")`.
</details>
<details><summary>Hint 2 (approach)</summary>

`mappedBy` names the *field* on the other side (`Bid.load`), not a column. `optional = false` makes the foreign-key column `NOT NULL`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "load_id")
private Load load;
```
</details>

## TODO 2: `placeBid`
<details><summary>Hint 1 (syntax)</summary>

`loadService.get(loadId)` and `carrierService.get(id)` already throw the right 404 exceptions. `bidRepository.existsByLoadIdAndCarrierIdAndStatus(...)` is given.
</details>
<details><summary>Hint 2 (approach)</summary>

Do the lookups first (load, then carrier), then the three `if` checks in the order listed. Create the bid with `clock.instant()`, call `load.addBid(bid)`, save it, and return `BidResponse.from(saved)`, still inside the method.
</details>
<details><summary>Hint 3 (code)</summary>

```java
if (load.getWeightKg() > carrier.getMaxWeightKg()) {
    throw new InvalidBidException("Carrier " + carrier.getId() + " can carry at most " + carrier.getMaxWeightKg()
            + " kg, but load " + loadId + " weighs " + load.getWeightKg() + " kg");
}
```
</details>

## TODO 3: `acceptBid`
<details><summary>Hint 1 (syntax)</summary>

`bid.getLoad()` gives the load (a lazy proxy: fine, you're inside the transaction). Then `load.getBids()`.
</details>
<details><summary>Hint 2 (approach)</summary>

Do every check *before* changing anything. It's clearer, and it means a failed check leaves nothing half-done, even before the rollback. Then accept, reject the other pending bids (compare with `!=`: within one transaction each row is exactly one Java object), and book the load. No `save` calls.
</details>
<details><summary>Hint 3 (code)</summary>

```java
bid.accept();
load.getBids().stream()
        .filter(other -> other != bid && other.getStatus() == BidStatus.PENDING)
        .forEach(Bid::reject);
load.book();
return BidResponse.from(bid);
```
</details>

## TODO 4: `bidsForLoad`
<details><summary>Hint 1 (syntax)</summary>

`@Transactional(readOnly = true)` and `bidRepository.findByLoadIdOrderByAmountPenceAscIdAsc(loadId)`.
</details>
<details><summary>Hint 2 (approach)</summary>

An unknown load id would give an empty list, so call `loadService.get(loadId)` first, only for its 404.
</details>
<details><summary>Hint 3 (code)</summary>

```java
loadService.get(loadId);
return bidRepository.findByLoadIdOrderByAmountPenceAscIdAsc(loadId).stream().map(BidResponse::from).toList();
```
</details>

## TODO 5: N+1
<details><summary>Hint 1 (syntax)</summary>

`@EntityGraph(attributePaths = "bids")` from `org.springframework.data.jpa.repository`, on an abstract method (delete `default` and the body).
</details>
<details><summary>Hint 2 (approach)</summary>

In `summaries`, map each load to a `LoadBidSummary`. The count is `load.getBids().size()`. The lowest pending bid is filter → map to amount → `min` → `orElse(null)`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
load.getBids().stream()
        .filter(bid -> bid.getStatus() == BidStatus.PENDING)
        .map(Bid::getAmountPence)
        .min(Long::compare)
        .orElse(null)
```
</details>

## TODO 6: delete guard and error mappings
<details><summary>Hint 1 (syntax)</summary>

`@Transactional` on `LoadService.delete`, because you'll read the lazy `getBids()`. `repository.delete(load)` deletes an entity you already have.
</details>
<details><summary>Hint 2 (approach)</summary>

`get(id)` gives the 404 for free. For the three new handlers, copy the `loadNotFound` method's pattern. A small private helper `problem(status, title, exception)` avoids repeating it three times.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@ExceptionHandler(InvalidBidException.class)
public ProblemDetail invalidBid(InvalidBidException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    problem.setTitle("Invalid bid");
    return problem;
}
```
</details>
