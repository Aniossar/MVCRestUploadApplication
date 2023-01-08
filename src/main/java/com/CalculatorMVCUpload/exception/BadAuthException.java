package com.CalculatorMVCUpload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Login/password are incorrect")
public class BadAuthException extends RuntimeException {

    public BadAuthException(String message) {
        super(message);
    }

    public BadAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
