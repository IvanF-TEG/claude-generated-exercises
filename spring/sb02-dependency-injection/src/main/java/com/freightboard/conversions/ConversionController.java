package com.freightboard.conversions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// SB01 step 4b: POST /api/conversions/weight takes a JSON body {"kg": 1500} and returns
//          {"kg":1500,"tonnes":1.5,"vehicle":"rigid"}.
// SB01 step 5b: kg of 0 or less -> 400 Bad Request with an empty body.
@RestController
public class ConversionController {

    @PostMapping("/api/conversions/weight")
    public ResponseEntity<WeightConversion> convert(@RequestBody WeightRequest request) {
        if (request.kg() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(WeightConversion.of(request.kg()));
    }
}
