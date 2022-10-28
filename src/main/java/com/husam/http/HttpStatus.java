package com.husam.http;

public enum HttpStatus {
    BAD_REQUEST(400, "Bad Request"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    URI_TOO_LONG(414, "URI Too Long"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    LENGTH_REQUIRED(411, "Length Required"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),
    HTTP_OK(200, "OK");


    public final int STATUS_CODE;
    public final String REASON_PHRASE;

    HttpStatus(int STATUS_CODE, String REASON_PHRASE) {
        this.STATUS_CODE = STATUS_CODE;
        this.REASON_PHRASE = REASON_PHRASE;
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }

    public String getReasonPhrase() {
        return REASON_PHRASE;
    }
}
