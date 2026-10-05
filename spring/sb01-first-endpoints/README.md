# SB01: First Endpoints (HTTP, REST and what Spring Boot does)

**Time box:** 75–90 min · **New concept:** HTTP and REST basics, `@RestController`, `@GetMapping`/`@PostMapping`, `@RequestParam`, `@PathVariable`, `@RequestBody`, `ResponseEntity`
**Review:** records (Ex 19), `IllegalArgumentException` (Ex 11), regular expressions

## Learning Objective
Build the first four endpoints of **FreightBoard**, the freight exchange API you'll grow throughout this track. By the end you'll know how an HTTP request finds its way to a Java method, how JSON becomes a record (and back), and how to choose the status code a client receives.

## Primer 1: HTTP in five minutes
Every request has a **method**, a **path** (with an optional **query string**), **headers** and an optional **body**. Every response has a **status code**, headers and an optional body.

```
POST /api/conversions/weight HTTP/1.1          <- method, path
Content-Type: application/json                 <- header: "my body is JSON"
Accept: application/json                       <- header: "please answer in JSON"

{"kg": 1500}                                   <- body

HTTP/1.1 200 OK                                <- status code
Content-Type: application/json

{"kg":1500,"tonnes":1.5,"vehicle":"rigid"}
```

| Method | Meaning | Safe to repeat? | Typical success code |
|---|---|---|---|
| `GET` | Read something | Yes | 200 OK |
| `POST` | Create something, or run an action | No | 201 Created (create) or 200 OK (action) |
| `PUT` | Replace something | Yes (same result each time) | 200 OK |
| `DELETE` | Remove something | Yes | 204 No Content |

**Status code families:** `2xx` it worked · `4xx` the *client* got something wrong (bad input, not found, not allowed) · `5xx` the *server* broke. A crash in your code shows up as 500. That's why TODO 5 turns invalid input into a 400: it's the client's mistake, not yours.

**REST** is a style, not a library: URLs name *things* (`/api/loads/42`), and the HTTP method says what to do with them. So you write `DELETE /api/loads/42`, not `GET /api/deleteLoad?id=42`.

**Where input can come from:**
| Part of the request | Example | Spring annotation |
|---|---|---|
| Path | `/api/postcodes/LS1` | `@PathVariable String outwardCode` |
| Query string | `/api/greeting?name=Ana` | `@RequestParam String name` |
| Body | `{"kg": 1500}` | `@RequestBody WeightRequest request` |

## Primer 2: what Spring Boot actually does
If you've used Flask, this will feel familiar:

| Flask (Python) | Spring Boot (Java) |
|---|---|
| `app = Flask(__name__)` | `@SpringBootApplication` class + `SpringApplication.run(...)` |
| `@app.get("/api/status")` | `@GetMapping("/api/status")` on a method in a `@RestController` class |
| `return {"status": "UP"}` (dict → JSON) | `return new StatusResponse(...)` (record → JSON, via **Jackson**) |
| `request.args.get("name", "guest")` | `@RequestParam(defaultValue = "guest") String name` |
| `request.get_json()` | `@RequestBody WeightRequest request` |
| `return "", 400` | `return ResponseEntity.badRequest().build();` |
| `flask run` | `mvn spring-boot:run` (starts an **embedded Tomcat** web server on port 8080) |

Three ideas make Boot "just work":
1. **Starters.** `spring-boot-starter-webmvc` in `pom.xml` pulls in Spring MVC, Tomcat and Jackson at versions that are known to work together.
2. **Auto-configuration.** At start-up, Boot sees Tomcat and Spring MVC on the classpath and configures a web server, JSON conversion and error handling for you. You only write the parts that are specific to your app.
3. **Component scanning.** Classes annotated with `@RestController` (and, in SB02, `@Service` and `@Component`) under the application's package are found and created automatically. You never write `new StatusController()`.

> 💡 **The magic, without the magic.** Here's roughly what Boot saves you, using only the JDK's built-in server:
> ```java
> HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
> server.createContext("/api/status", exchange -> {
>     if (!exchange.getRequestMethod().equals("GET")) { exchange.sendResponseHeaders(405, -1); return; }
>     byte[] body = "{\"service\":\"FreightBoard\",\"status\":\"UP\"}".getBytes();
>     exchange.getResponseHeaders().add("Content-Type", "application/json");
>     exchange.sendResponseHeaders(200, body.length);
>     exchange.getResponseBody().write(body);
>     exchange.close();
> });
> server.start();
> ```
> Now imagine doing that for 30 endpoints, with JSON parsing, content negotiation and error pages. That's the work Spring MVC does for you.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `status/StatusController.java` | Make it a `@RestController`; `GET /api/status` returns a `StatusResponse` |
| 2 | `status/StatusController.java` | `GET /api/greeting` with an optional `name` query parameter |
| 3 | `postcodes/PostcodeInfo.java`, `PostcodeController.java` | Parse an outward code (plain Java), then expose it at `GET /api/postcodes/{outwardCode}` |
| 4 | `conversions/WeightConversion.java`, `ConversionController.java` | Vehicle rules (plain Java), then `POST /api/conversions/weight` with a JSON body |
| 5 | `PostcodeController.java`, `ConversionController.java` | Invalid input → `400 Bad Request` using `ResponseEntity` |
| Predictions | `src/test/java/com/freightboard/PredictionsTest.java` | Replace every `-1` / `"???"` *after* TODOs 1–5 but *before* running it |

Suggested order: **3a → 4a** (plain Java, fast feedback) **→ 1 → 2 → 3b → 4b → 5 → Predictions**.

## How to Run
**Tests:**
- **IntelliJ:** right-click `spring/pom.xml` → **Add as Maven Project** (one time only). Then click ▶ next to any test class or `@Nested` group.
- **Terminal** (from the `spring` folder):
  ```bash
  mvn -q test -pl sb01-first-endpoints                               # the whole exercise
  mvn -q test -pl sb01-first-endpoints -Dtest='PostcodeInfoTest'     # one class
  ```

**The real app:**
```bash
mvn -q spring-boot:run -pl sb01-first-endpoints
```
Then, in a second terminal:
```bash
curl -i localhost:8080/api/status
curl -i "localhost:8080/api/greeting?name=Ana"
curl -i localhost:8080/api/postcodes/ec1a
curl -i -X POST localhost:8080/api/conversions/weight -H 'Content-Type: application/json' -d '{"kg": 1500}'
```
`-i` prints the status line and headers as well as the body. Stop the app with **Ctrl+C**. (In IntelliJ you can also run `FreightBoardApplication` with ▶.)

## Test Cases
| Request | Expected |
|---|---|
| `GET /api/status` | `200`, `{"service":"FreightBoard","status":"UP"}` |
| `GET /api/greeting` | `200`, `Welcome to FreightBoard, guest!` (plain text) |
| `GET /api/postcodes/ec1a` | `200`, `{"outwardCode":"EC1A","area":"EC","london":true}` |
| `GET /api/postcodes/123` | `400`, empty body |
| `POST /api/conversions/weight` with `{"kg": 1500}` | `200`, `{"kg":1500,"tonnes":1.5,"vehicle":"rigid"}` |
| `POST /api/conversions/weight` with `{"kg": 0}` | `400`, empty body |

The full run: **39 tests, 0 failures** (including seven predictions).

## Definition of Done
- [ ] 39/39 green
- [ ] Every `curl` command above gives the expected result against the **running** app
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: remove `@RestController` from `StatusController` (keep `@GetMapping`) and run `StatusControllerTest`. What status code do you get, and why? Put it back.
- [ ] Experiment: move `FreightBoardApplication` into a new package `com.freightboard.app` and run the tests, then start the app and `curl` `/api/status`. Two different things break. Tests search *upwards* from their own package for the `@SpringBootApplication` class, and component scanning searches *downwards* from it. Explain each failure, then move the class back.
- [ ] Written note (interview-style): "What's the difference between `@PathVariable`, `@RequestParam` and `@RequestBody`? When would you use each?"
- [ ] Written note: "Why should invalid input return 400 rather than 500?"

Stuck? See [HINTS.md](HINTS.md).
