package com.husam.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse implements HttpMessage {
    private HttpVersion httpVersion;

    private HttpStatus httpStatus;
    private HttpHeaders headers;
    private byte[] body;

    public HttpResponse(HttpResponseBuilder builder) {
        this.httpVersion = builder.httpVersion;
        this.httpStatus = builder.httpStatus;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static class HttpResponseBuilder {
        private HttpVersion httpVersion;
        private HttpStatus httpStatus;
        private HttpHeaders headers;
        private byte[] body;

        public HttpResponseBuilder() {
            body = "".getBytes();
        }


        public HttpResponseBuilder setHttpVersion(HttpVersion httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public HttpResponseBuilder setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public HttpResponseBuilder setHttpHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public HttpResponseBuilder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] build() {
        final String CRLF = "\r\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(body.length));
        headers.put("Content-Length", list);
        String allHeaders = headers.toString();
        try {
            outputStream.write((httpVersion.getLiteral() + " " + httpStatus.getStatusCode() + " " + httpStatus.getReasonPhrase() + CRLF +  // Status Line  :   HTTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                    allHeaders+ CRLF).getBytes());
            outputStream.write(body);
            outputStream.write((CRLF + CRLF).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }
}
