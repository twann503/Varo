package com.alex.varo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionMessageResponse {

    /**
     * Sends Custom Resource Not Found Error
     *
     * @param e          the thrown exception
     * @param webRequest the REST request
     * @return the error message sent to consumer
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException e, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                e.getMessage(),
                webRequest.getDescription(false).split("=")[1]
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Sends Internal Error Message
     * whenever a uncaught exception is thrown
     *
     * @param e          the thrown exception
     * @param webRequest the REST request
     * @return the error message sent to consumer
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> internalExceptionHandler(Exception e, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                e.getLocalizedMessage(),
                webRequest.getDescription(false).split("=")[1]
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
