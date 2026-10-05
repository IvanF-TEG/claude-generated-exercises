package com.freightboard.loads;

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
import java.util.List;

/**
 * TODO 2: add @Valid to the two @RequestBody parameters, so Spring validates them BEFORE the method runs.
 * TODO 5: the service now returns Load (or throws) instead of Optional. Simplify the methods to match:
 *         no more ResponseEntity.of(...). Delete returns void with @ResponseStatus(HttpStatus.NO_CONTENT).
 */
@RestController
@RequestMapping("/api/loads")
public class LoadController {

    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    @GetMapping
    public List<Load> search(@RequestParam(required = false) LoadStatus status,
                             @RequestParam(required = false) String origin) {
        return loadService.search(status, origin);
    }
    
    @PostMapping
    public ResponseEntity<Load> create(@RequestBody LoadRequest request) {
        Load created = loadService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
    
    // The SB04 versions of get, update, cancel and delete no longer compile against the new LoadService,
    // so they've been removed. Write them again, simpler (TODO 5).
}
