package com.revnext.service.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InSuffiicientStockException extends RuntimeException {
    public InSuffiicientStockException(String message) {
        super(message);
    }
}