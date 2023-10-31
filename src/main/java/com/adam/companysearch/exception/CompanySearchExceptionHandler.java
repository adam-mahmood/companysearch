package com.adam.companysearch.exception;

import com.adam.companysearch.model.ErrorDetails;
import com.adam.companysearch.model.JSONAPIErrorDetails;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class CompanySearchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CompanySearchNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> handleNotFoundException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getErrorDetails(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false)),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleBadRequestException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getErrorDetails(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignStatusException(FeignException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.status());
        return new ResponseEntity<>(getErrorDetails(httpStatus, e.getMessage(), null),
                httpStatus);
    }
    private ErrorDetails getErrorDetails(final HttpStatus status, final String message, final String requestDescription){
        ErrorDetails errorDetails = new ErrorDetails();
        JSONAPIErrorDetails jsonapiErrorDetails = new JSONAPIErrorDetails()
                .code(status.value())
                .details(requestDescription)
                .message(message);
        errorDetails.addErrorsItem(jsonapiErrorDetails);
        return errorDetails;
    }
}
