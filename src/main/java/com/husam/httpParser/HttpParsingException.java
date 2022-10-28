package com.husam.httpParser;

import com.husam.http.HttpStatus;

public class HttpParsingException extends Exception {
    private final HttpStatus httpStatus;

    public HttpParsingException(HttpStatus httpStatus) {
        super(httpStatus.REASON_PHRASE);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
