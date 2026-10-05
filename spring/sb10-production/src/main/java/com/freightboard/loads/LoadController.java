package com.freightboard.loads;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * SB06 step 6a: every endpoint returns LoadResponse (or List<LoadResponse>), never the Load entity.
 *          Convert with LoadResponse.from, e.g. list.stream().map(LoadResponse::from).toList()
 * SB06 step 6b: two new endpoints
 *   GET /api/loads/pickups?from=2031-03-01&to=2031-03-31  -> pickupsBetween. The dates are ISO format:
 *                                                           add @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 *   GET /api/loads/heaviest?limit=3                         -> heaviestOpen, limit defaults to 5
 */
/**
 * SB09 step 6a: create, update and cancel pass the logged-in user's name (Principal) to the service.
 */
@RestController
@RequestMapping("/api/loads")
public class LoadController {

    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    @GetMapping
    public List<LoadResponse> search(@RequestParam(required = false) LoadStatus status,
                                     @RequestParam(required = false) String origin) {
        return toResponses(loadService.search(status, origin));
    }

    @GetMapping("/pickups")
    public List<LoadResponse> pickups(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return toResponses(loadService.pickupsBetween(from, to));
    }

    @GetMapping("/heaviest")
    public List<LoadResponse> heaviest(@RequestParam(defaultValue = "5") int limit) {
        return toResponses(loadService.heaviestOpen(limit));
    }

    // SB08 step 4d (part 2): GET /api/loads/by-reference/{reference} -> the load, or 404
    @GetMapping("/by-reference/{reference}")
    public LoadResponse getByReference(@PathVariable String reference) {
        return LoadResponse.from(loadService.getByReference(reference));
    }

    @GetMapping("/{id}")
    public LoadResponse get(@PathVariable long id) {
        return LoadResponse.from(loadService.get(id));
    }

    @PostMapping
    public ResponseEntity<LoadResponse> create(@Valid @RequestBody LoadRequest request, Principal principal) {
        LoadResponse created = LoadResponse.from(loadService.create(request, principal.getName()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public LoadResponse update(@PathVariable long id, @Valid @RequestBody LoadRequest request, Principal principal) {
        return LoadResponse.from(loadService.update(id, request, principal.getName()));
    }

    @PostMapping("/{id}/cancel")
    public LoadResponse cancel(@PathVariable long id, Principal principal) {
        return LoadResponse.from(loadService.cancel(id, principal.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        loadService.delete(id);
    }

    private static List<LoadResponse> toResponses(List<Load> loads) {
        return loads.stream().map(LoadResponse::from).toList();
    }
}
