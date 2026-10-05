package com.freightboard.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * ONE place that turns exceptions into HTTP error responses, for every controller.
 * Every error body uses the standard "Problem Details" format (RFC 9457), with Content-Type application/problem+json:
 *   {"type":"about:blank","title":"Load not found","status":404,"detail":"No load with id 99","instance":"/api/loads/99"}
 *
 * Extending ResponseEntityExceptionHandler (GIVEN) means Spring's OWN errors (malformed JSON, 405, 415...) come
 * back in the same format.
 *
 * TODO 6a: LoadNotFoundException      -> 404, title "Load not found",     detail = the exception's message
 * TODO 6b: InvalidLoadStateException  -> 409, title "Invalid load state", detail = the exception's message
 *          (an @ExceptionHandler method can simply RETURN a ProblemDetail: ProblemDetail.forStatusAndDetail(...))
 * TODO 6c: validation failures (see the method below)
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * TODO 6c: runs when a @Valid @RequestBody fails validation. Spring has already built a 400 ProblemDetail for
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
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
