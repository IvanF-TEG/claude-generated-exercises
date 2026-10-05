package com.freightboard.postcodes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// SB01 step 3b: GET /api/postcodes/{outwardCode} returns the PostcodeInfo as JSON. The code comes from the PATH, not a query parameter.
// SB01 step 5a: an invalid code (e.g. /api/postcodes/123) must return 400 Bad Request with an empty body, not 500.
//          Change the return type to ResponseEntity<PostcodeInfo> so you can choose the status code.
@RestController
public class PostcodeController {

    @GetMapping("/api/postcodes/{outwardCode}")
    public ResponseEntity<PostcodeInfo> lookup(@PathVariable String outwardCode) {
        try {
            return ResponseEntity.ok(PostcodeInfo.from(outwardCode));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
