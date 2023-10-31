package com.adam.companysearch.exception;

public class CompanySearchNotFoundException extends RuntimeException {

    public CompanySearchNotFoundException(String message) {
        super(message);
    }

    public CompanySearchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
