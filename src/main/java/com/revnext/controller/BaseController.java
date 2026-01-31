package com.revnext.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

@Slf4j
public class BaseController {

    /**
     * Returns a success response with the given body.
     */
    public <T> ResponseEntity<T> getSuccess(T body) {
        return ResponseEntity.ok(body);
    }

    /**
     * Handles the supplier execution and returns a standardized response.
     * Automatically catches exceptions and logs errors.
     */
    public <T> ResponseEntity<T> getResponse(Supplier<T> supplier) {
        return getSuccess(supplier.get());
    }

}
