package com.freightboard.loads;

import jakarta.validation.Valid;
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
 * TODO 6a: every endpoint returns LoadResponse (or List<LoadResponse>), never the Load entity.
 *          Convert with LoadResponse.from, e.g. list.stream().map(LoadResponse::from).toList()
 * TODO 6b: two new endpoints
 *   GET /api/loads/pickups?from=2031-03-01&to=2031-03-31  -> pickupsBetween. The dates are ISO format:
 *                                                           add @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 *   GET /api/loads/heaviest?limit=3                         -> heaviestOpen, limit defaults to 5
 */
@RestController
@RequestMapping("/api/loads")
public class LoadController {

    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }
    
    // The SB05 methods returned Load, which is now an entity. Rewrite all six to return LoadResponse (TODO 6a),
    // then add the two new endpoints (TODO 6b). SB05's versions are in the sb05 module if you want to start from them.
}
