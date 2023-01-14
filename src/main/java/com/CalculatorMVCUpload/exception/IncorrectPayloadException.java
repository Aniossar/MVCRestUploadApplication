package com.CalculatorMVCUpload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Incorrect payload of request")
public class IncorrectPayloadException extends RuntimeException{
    public IncorrectPayloadException(String message) {
        super(message);
    }

    public IncorrectPayloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
