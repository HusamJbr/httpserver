package com.husam.http;

public class HttpRequest implements HttpMessage {
    private HttpMethod httpMethod;
    private HttpVersion httpVersion;
    private String URI;
    private HttpHeaders headers;

    private String body;

    private HttpRequest(HttpRequestBuilder builder) {
        this.httpMethod = builder.httpMethod;
        this.httpVersion = builder.httpVersion;
        this.URI = builder.URI;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static class HttpRequestBuilder {
        private HttpMethod httpMethod;
        private HttpVersion httpVersion;
        private String URI;
        private HttpHeaders headers;
        private String body;

        public HttpRequestBuilder() {

        }

        public HttpRequestBuilder setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public HttpRequestBuilder setHttpVersion(HttpVersion httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public HttpRequestBuilder setURI(String URI) {
            this.URI = URI;
            return this;
        }

        public HttpRequestBuilder setHttpHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public String getURI() {
        return URI;
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
