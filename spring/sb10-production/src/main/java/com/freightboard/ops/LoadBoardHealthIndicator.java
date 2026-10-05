package com.freightboard.ops;

import com.freightboard.loads.LoadRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * A custom part of /actuator/health, shown as "loadBoard" (the bean name without the "HealthIndicator" suffix).
 *
 * TODO 2: can FreightBoard read its load board right now?
 *   - count the OPEN loads with the repository (countByStatus)
 *   - it worked -> Health.up().withDetail("openLoads", count).build()
 *   - it threw  -> Health.down(exception).build()     (catch Exception: we're reporting on failure, not causing it)
 *
 * Health should mean "can this instance serve requests?", NOT "is business going well?". An orchestrator such as
 * Kubernetes may RESTART an instance that reports DOWN, and restarting won't create more loads.
 */
@Component
public class LoadBoardHealthIndicator implements HealthIndicator {

    private final LoadRepository loadRepository;

    public LoadBoardHealthIndicator(LoadRepository loadRepository) {
        this.loadRepository = loadRepository;
    }

    @Override
    public Health health() {
        return Health.unknown().build();
    }
}
