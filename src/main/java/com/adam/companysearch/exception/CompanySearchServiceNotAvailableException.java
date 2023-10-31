package com.adam.companysearch.exception;

public class CompanySearchServiceNotAvailableException extends RuntimeException {

    public CompanySearchServiceNotAvailableException(String message) {
        super(message);
    }

    public CompanySearchServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
