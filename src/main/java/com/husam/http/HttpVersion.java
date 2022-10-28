package com.husam.http;

import com.husam.http.exception.BadHttpVersionException;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    public final String LITERAL;

    HttpVersion(String LITERAL) {
        this.LITERAL = LITERAL;
    }

    public static HttpVersion getCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            }
        }
        throw new BadHttpVersionException();
    }

    public String getLiteral() {
        return LITERAL;
    }
}
