package com.husam.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HttpHeaders implements Map<String, List<String>> {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpHeaders.class);
    private Map<String, List<String>> headers;

    public HttpHeaders() {
        headers = new HashMap<>();
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return headers.get(key);
    }

    public String getFirst(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (!headers.containsKey(key)) {
            return null;
        }
        List<String> value = headers.get(key);
        if (value.size() == 0) {
            return null;
        } else {
            return value.get(0);
        }
    }

    public void add(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        if (headers.containsKey(key)) {
            List<String> values = headers.get(key);
            values.add(value);
        } else {
            List<String> values = new ArrayList<>();
            values.add(value);
            headers.put(key, values);
        }
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        headers.putAll(m);
    }

    @Override
    public void clear() {
        headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return headers.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final String CRLF = "\r\n";
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            sb.append(key+":");
            sb.append(" "+value+CRLF);
        }
        return sb.toString();
    }
}
