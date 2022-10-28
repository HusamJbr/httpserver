package com.husam.http;

import com.husam.http.exception.BadHttpMethodException;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int tempMaxLength = 0;
        for (HttpMethod method : values()) {
            tempMaxLength = Math.max(tempMaxLength, method.name().length());
        }
        MAX_LENGTH = tempMaxLength;
    }

    public static HttpMethod getMethod(String httpMethod) throws BadHttpMethodException {
        try {
            return HttpMethod.valueOf(httpMethod);
        } catch (Exception e) {
            throw new BadHttpMethodException();
        }
    }

}
