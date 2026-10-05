package com.freightboard.errors;

import com.freightboard.bids.BidNotFoundException;
import com.freightboard.bids.InvalidBidException;
import com.freightboard.carriers.CarrierNotFoundException;
import com.freightboard.loads.InvalidLoadStateException;
import com.freightboard.loads.LoadNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ONE place that turns exceptions into HTTP error responses, for every controller.
 * Every error body uses the standard "Problem Details" format (RFC 9457), with Content-Type application/problem+json:
 *   {"type":"about:blank","title":"Load not found","status":404,"detail":"No load with id 99","instance":"/api/loads/99"}
 *
 * Extending ResponseEntityExceptionHandler (GIVEN) means Spring's OWN errors (malformed JSON, 405, 415...) come
 * back in the same format.
 *
 * SB05 step 6a: LoadNotFoundException      -> 404, title "Load not found",     detail = the exception's message
 * SB05 step 6b: InvalidLoadStateException  -> 409, title "Invalid load state", detail = the exception's message
 *          (an @ExceptionHandler method can simply RETURN a ProblemDetail: ProblemDetail.forStatusAndDetail(...))
 * SB05 step 6c: validation failures (see the method below)
 *
 * SB07 step 6b: three more mappings, in the same style:
 *   CarrierNotFoundException -> 404, title "Carrier not found"
 *   BidNotFoundException     -> 404, title "Bid not found"
 *   InvalidBidException      -> 409, title "Invalid bid"
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoadNotFoundException.class)
    public ProblemDetail loadNotFound(LoadNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Load not found");
        return problem;
    }

    @ExceptionHandler(CarrierNotFoundException.class)
    public ProblemDetail carrierNotFound(CarrierNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, "Carrier not found", e);
    }

    @ExceptionHandler(BidNotFoundException.class)
    public ProblemDetail bidNotFound(BidNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, "Bid not found", e);
    }

    @ExceptionHandler(InvalidBidException.class)
    public ProblemDetail invalidBid(InvalidBidException e) {
        return problem(HttpStatus.CONFLICT, "Invalid bid", e);
    }

    private static ProblemDetail problem(HttpStatus status, String title, RuntimeException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        problem.setTitle(title);
        return problem;
    }

    @ExceptionHandler(InvalidLoadStateException.class)
    public ProblemDetail invalidState(InvalidLoadStateException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problem.setTitle("Invalid load state");
        return problem;
    }

    /**
     * SB05 step 6c: runs when a @Valid @RequestBody fails validation. Spring has already built a 400 ProblemDetail for
     * you: ex.getBody(). Change it to
     *   title  "Validation failed"
     *   detail "The request has 2 invalid field(s)"        (count the FIELDS, not the messages)
     *   and add a property "errors": field name -> list of messages, fields in alphabetical order, each list sorted:
     *   "errors": {"origin": ["must be a UK outward code such as LS1", "must not be blank"], "weightKg": ["must be greater than 0"]}
     * Then return ResponseEntity.status(status).headers(headers).body(problem).
     * The messages are in ex.getBindingResult().getFieldErrors(): each has getField() and getDefaultMessage().
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> errors = new TreeMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.computeIfAbsent(error.getField(), field -> new ArrayList<>()).add(error.getDefaultMessage()));
        errors.values().forEach(messages -> messages.sort(null));

        ProblemDetail problem = ex.getBody();
        problem.setTitle("Validation failed");
        problem.setDetail("The request has " + errors.size() + " invalid field(s)");
        problem.setProperty("errors", errors);
        return ResponseEntity.status(status).headers(headers).body(problem);
    }
}
