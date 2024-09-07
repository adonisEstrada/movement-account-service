package com.adonis.nttdata.exeption;

import org.springframework.http.HttpStatus;

public class ChallengeException extends RuntimeException {
    private HttpStatus httpStatus;

    public ChallengeException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse getExceptionResponse() {
        return ExceptionResponse.builder()
                .message(getLocalizedMessage())
                .build();
    }
}
