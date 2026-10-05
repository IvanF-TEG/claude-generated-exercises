# SB06 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: entity mapping
<details><summary>Hint 1 (syntax)</summary>

All from `jakarta.persistence`: `@Entity`, `@Table(name = "loads")`, `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`, `@Column(nullable = false)`, `@Enumerated(EnumType.STRING)`.
</details>
<details><summary>Hint 2 (approach)</summary>

Class annotations go on the class; everything else goes on the fields. `nullable = false` is what generates `NOT NULL` in the table. Bean Validation (SB05) checks requests, and this protects the table itself.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@Entity
@Table(name = "loads")
public class Load {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ...
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status;
```
</details>

## TODO 2: derived queries
<details><summary>Hint 1 (syntax)</summary>

An interface method with no body ends in `;`: `List<Load> findByStatusOrderByIdAsc(LoadStatus status);`
</details>
<details><summary>Hint 2 (approach)</summary>

The names are already correct. The work is deleting `default` and the `{ ... }` body. Spring Data only implements methods that are *abstract*. A `default` method has a body, so Spring Data leaves it alone (and it throws `UnsupportedOperationException`).
</details>
<details><summary>Hint 3 (code)</summary>

```java
List<Load> findAllByOrderByIdAsc();
List<Load> findByStatusOrderByIdAsc(LoadStatus status);
long countByStatus(LoadStatus status);
```
</details>

## TODO 3: JPQL
<details><summary>Hint 1 (syntax)</summary>

`@Query("select l from Load l where ... order by ...")` from `org.springframework.data.jpa.repository`. A method parameter `status` is referenced as `:status`.
</details>
<details><summary>Hint 2 (approach)</summary>

Use the *entity* name (`Load`) and *field* names (`l.weightKg`), not the table and column names. Sort by weight descending, then by id ascending for ties. The `Limit` parameter is applied for you: don't write `LIMIT` in the query.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@Query("select l from Load l where l.status = :status order by l.weightKg desc, l.id asc")
List<Load> findHeaviest(LoadStatus status, Limit limit);
```
</details>

## TODO 5: service
<details><summary>Hint 1 (syntax)</summary>

`org.springframework.transaction.annotation.Transactional` on the method.
</details>
<details><summary>Hint 2 (approach)</summary>

`search` is four cases: both filters, only status, only origin, neither. For `update`/`cancel`: load it with the existing `openLoad(id)`, call the entity's method, and return the entity. Commit happens when the method returns.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@Transactional
public Load cancel(long id) {
    Load load = openLoad(id);
    load.cancel();
    return load;
}
```
</details>

## TODO 6: controller
<details><summary>Hint 1 (syntax)</summary>

`loads.stream().map(LoadResponse::from).toList()`. For dates: `@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from`.
</details>
<details><summary>Hint 2 (approach)</summary>

Write one small `private static List<LoadResponse> toResponses(List<Load> loads)` helper and use it in all three list endpoints. `/pickups` and `/heaviest` don't clash with `/{id}`: literal paths win over templates.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@GetMapping("/heaviest")
public List<LoadResponse> heaviest(@RequestParam(defaultValue = "5") int limit) {
    return toResponses(loadService.heaviestOpen(limit));
}
```
</details>
