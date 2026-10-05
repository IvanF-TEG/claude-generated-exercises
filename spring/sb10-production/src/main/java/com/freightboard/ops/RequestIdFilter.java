package com.freightboard.ops;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Gives every request an id, so all the log lines for ONE request can be found together, even when hundreds of
 * requests are interleaved in the log. If the caller (or a load balancer in front of us) already sent an
 * X-Request-Id header, keep it, so the id follows the request across services.
 *
 * HIGHEST_PRECEDENCE: this filter runs before Spring Security's, so even a 401 response carries the id.
 *
 * TODO 5a: in doFilterInternal:
 *   - requestId = the X-Request-Id header if present and not blank, otherwise a new random UUID string
 *   - MDC.put(MDC_KEY, requestId): the MDC is a per-thread map that the logger adds to every line (TODO 5c)
 *   - response.setHeader(HEADER, requestId)
 *   - chain.doFilter(request, response)
 * TODO 5b: ALWAYS remove the key afterwards (in a finally block). Web server threads are REUSED: without it, the
 *          next request on this thread would log with the previous request's id.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(request, response);
    }
}
