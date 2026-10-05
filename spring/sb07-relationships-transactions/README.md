# SB07: Relationships and Transactions

**Time box:** 90–120 min · **New concept:** `@ManyToOne` / `@OneToMany(mappedBy)`, foreign keys, `FetchType.LAZY`, `LazyInitializationException`, transaction boundaries, all-or-nothing updates, rollback rules, self-invocation, the N+1 query problem and `@EntityGraph`
**Review:** SB06 entities and dirty checking, SB05 exceptions and problem details, `Optional`/streams (Ex 17–18)

## Learning Objective
FreightBoard becomes a marketplace: **carriers bid on loads**, and a shipper **accepts** one bid. That single action must change several rows *together* (the bid is accepted, the rest are rejected, the load is booked), or not at all. You'll map relationships between tables, see exactly when lazy loading works and when it blows up, and find and fix the most common JPA performance bug.

## Primer 1: relationships
In the database, a bid "belongs to" a load through a **foreign key**, a column holding the other row's id:
```
loads                bids                                   carriers
+----+--------+     +----+---------+------------+--------+  +----+-----------------+
| id | origin |     | id | load_id | carrier_id | amount |  | id | name            |
|  7 | LS1    |<----|  5 |    7    |     3      |  9000  |->|  3 | Pennine Haulage |
+----+--------+     |  6 |    7    |     4      |  8000  |  +----+-----------------+
                    +----+---------+------------+--------+
```
| Java side | Annotation | Owns the foreign key? |
|---|---|---|
| `Bid.load` (many bids → one load) | `@ManyToOne` + `@JoinColumn(name = "load_id")` | **Yes**: this is the side JPA writes |
| `Load.bids` (one load → many bids) | `@OneToMany(mappedBy = "load")` | No: it's a read-only mirror of `Bid.load` |

Keep both sides in step in memory (`load.addBid(bid)`). The database only looks at `Bid.load`.

**Lazy vs eager.** `LAZY` means "don't load the related object until someone touches it". Touching it runs a query, which **needs an open transaction**. Outside a transaction you get `LazyInitializationException`. `@ManyToOne` is *eager* by default; make it lazy and load related data deliberately.

## Primer 2: transactions
A transaction is all-or-nothing: either every change commits, or none of them do. In Spring, a `@Transactional` method starts one when it's called (or joins one already running), then commits when it returns. It **rolls back** if it throws.

| Rule | Why it matters |
|---|---|
| Rolls back on **unchecked** exceptions (`RuntimeException`) only | A checked `Exception` still **commits**, unless you add `rollbackFor = Exception.class` |
| Works through a **proxy**: Spring wraps your bean | `this.otherMethod()` bypasses the proxy, so `@Transactional` on `otherMethod` is **ignored** ("self-invocation") |
| `readOnly = true` | A hint that lets Hibernate skip dirty checking. Use it for queries |
| The transaction ends when the method returns | Build DTOs *inside* it if they read lazy relationships |

Python analogue: `with session.begin():` in SQLAlchemy, except that Spring opens and closes the block for you around the method.

## Primer 3: the N+1 problem
```java
for (Load load : loadRepository.findByStatusOrderByIdAsc(OPEN)) {   // 1 query: all loads
    load.getBids().size();                                           // +1 query PER load
}
```
100 loads means 101 queries. It passes every test with 3 rows and falls over in production. The fix is to fetch the bids *with* the loads in one joined query: `@EntityGraph(attributePaths = "bids")` on the repository method (or `join fetch` in JPQL). `NPlusOneTest` counts the statements for real, using Hibernate's statistics.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `bids/Bid.java`, `loads/Load.java` | `@ManyToOne` (lazy, required, named join columns) and `@OneToMany(mappedBy)` |
| 2 | `bids/BidService.java` | `placeBid`: five rules, in order |
| 3 | `bids/BidService.java` | `acceptBid`: accept, reject the others, book the load, all in one transaction |
| 4 | `bids/BidService.java` | `bidsForLoad`, read-only, DTOs built inside the transaction |
| 5 | `loads/LoadRepository.java`, `bids/BidService.java` | `@EntityGraph` method, then `summaries` in **one** query |
| 6 | `loads/LoadService.java`, `errors/GlobalExceptionHandler.java` | Refuse to delete a load with bids (409); three new error mappings |
| Predictions | `src/test/java/com/freightboard/bids/PredictionsTest.java` | Rollback rules, self-invocation, lazy loading, counting statements |

`Carrier`, its repository/service/controller, `BidRepository`, `BidController` and the DTOs are given.

> ⚠️ **Do TODO 1 first.** Until `Bid.load` and `Bid.carrier` are mapped, Hibernate can't work out what to store for a field of type `Load`, and every Spring test errors at start-up. Run `BidMappingTest` to check TODO 1.

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb07-relationships-transactions
mvn -q spring-boot:run -pl sb07-relationships-transactions -Dspring-boot.run.profiles=sql
```
A whole auction with `curl`:
```bash
curl -s -X POST localhost:8080/api/carriers -H 'Content-Type: application/json' -d '{"name":"Pennine Haulage","maxWeightKg":20000}'
curl -s -X POST localhost:8080/api/carriers -H 'Content-Type: application/json' -d '{"name":"Dales Freight","maxWeightKg":44000}'
curl -s -X POST localhost:8080/api/loads -H 'Content-Type: application/json' \
     -d '{"origin":"LS1","destination":"M1","weightKg":1500,"pickupDate":"2031-03-01"}'
curl -s -X POST localhost:8080/api/loads/1/bids -H 'Content-Type: application/json' -d '{"carrierId":1,"amountPence":9000}'
curl -s -X POST localhost:8080/api/loads/1/bids -H 'Content-Type: application/json' -d '{"carrierId":2,"amountPence":8000}'
curl -s localhost:8080/api/loads/with-bids
curl -s -X POST localhost:8080/api/bids/2/accept
curl -s localhost:8080/api/loads/1/bids
```
With the `sql` profile, count the statements printed for `/api/loads/with-bids`. It should be one `select` with a `left join`.

## Test Cases
| Scenario | Expected |
|---|---|
| Bid from a carrier whose `maxWeightKg` is below the load's weight | `409`, `Carrier 3 can carry at most 1000 kg, but load 7 weighs 1500 kg` |
| Second pending bid from the same carrier | `409`, `Carrier 3 already has a pending bid on load 7` |
| Accept bid B when A is also pending | B `ACCEPTED`, A `REJECTED`, load `BOOKED` |
| Accept a bid on a cancelled load | `409`, and **nothing** changes |
| `summaries(OPEN)` for 4 loads | 1 SQL statement |
| `DELETE` a load with bids | `409`, `Load 7 has bids and can't be deleted` |

The full run: **168 tests, 0 failures** (including six predictions).

## Definition of Done
- [ ] 168/168 green
- [ ] The `curl` auction works, and `/api/loads/with-bids` runs one `select` in the `sql` log
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: remove `mappedBy = "load"` from `Load.bids` and run `BidMappingTest`. What extra table does Hibernate invent? Put it back.
- [ ] Experiment: remove `@Transactional(readOnly = true)` from `bidsForLoad`. Which exception do you get, and on which line? Put it back.
- [ ] Experiment: remove `@EntityGraph` (keep the method, add a `@Query("select l from Load l where l.status = :status order by l.id")`). How many statements does `NPlusOneTest` count now? Put it back.
- [ ] Written note (interview-style): "What is the N+1 problem, how do you spot it, and how do you fix it?"
- [ ] Written note: "Name two ways a `@Transactional` annotation can silently do nothing." (Hint: look at your predictions.)

Stuck? See [HINTS.md](HINTS.md).
