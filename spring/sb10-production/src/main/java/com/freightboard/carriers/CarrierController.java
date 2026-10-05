package com.freightboard.carriers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// GIVEN.
@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrierResponse create(@Valid @RequestBody CarrierRequest request) {
        return CarrierResponse.from(carrierService.create(request));
    }

    @GetMapping
    public List<CarrierResponse> all() {
        return carrierService.all().stream().map(CarrierResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CarrierResponse get(@PathVariable long id) {
        return CarrierResponse.from(carrierService.get(id));
    }
}
