package com.CalculatorMVCUpload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This login or email is already registered")
public class ExistingLoginEmailRegister extends RuntimeException {

    public ExistingLoginEmailRegister(String message) {
        super(message);
    }

    public ExistingLoginEmailRegister(String message, Throwable cause) {
        super(message, cause);
    }
}
