package com.husam.httpParser;

import com.husam.config.Configuration;
import com.husam.config.ConfigurationManager;
import com.husam.http.*;
import com.husam.http.exception.BadHttpMethodException;
import com.husam.http.exception.BadHttpVersionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20; // Space
    private static final int CR = 0x0D; // Carriage return
    private static final int LF = 0x0A; // Line feed
    private static final int COLON = 0x3A;

    private static Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

    private int contentLength;

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        HttpRequest.HttpRequestBuilder requestBuilder = new HttpRequest.HttpRequestBuilder();
        parseRequestLine(buffer, requestBuilder);
        parseHeaderLines(buffer, requestBuilder);
        parseEntityBody(buffer, requestBuilder);
        HttpRequest request = requestBuilder.build();
        return request;
    }


    private void parseRequestLine(BufferedReader buffer, HttpRequest.HttpRequestBuilder requestBuilder) throws IOException, HttpParsingException {
        StringBuilder sb = new StringBuilder();
        int _byte;
        ArrayList<String> tokens = new ArrayList<>();
        while ((_byte = buffer.read()) != -1) {
            if (tokens.size() >= 3) {
                throw new HttpParsingException(HttpStatus.BAD_REQUEST);
            }
            if (_byte == SP) {
                tokens.add(sb.toString());
                sb.setLength(0);
                continue;
            }
            if (tokens.size() == 0 && sb.length() > configuration.getMaxMethodSize()) {
                throw new HttpParsingException(HttpStatus.NOT_IMPLEMENTED);
            } else if (tokens.size() == 1 && sb.length() > configuration.getMaxUriSize()) {
                throw new HttpParsingException(HttpStatus.URI_TOO_LONG);
            } else if (tokens.size() == 2 && sb.length() > configuration.getMaxVersionSize()) {
                throw new HttpParsingException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
            }
            if (_byte != CR) {
                sb.append((char) _byte);
                continue;
            }
            _byte = buffer.read();
            if (_byte != LF) {
                throw new HttpParsingException(HttpStatus.BAD_REQUEST);
            }
            tokens.add(sb.toString());
            sb.setLength(0);
            break;
        }
        if (tokens.size() != 3) {
            throw new HttpParsingException(HttpStatus.BAD_REQUEST);
        }
        try {
            requestBuilder.setHttpMethod(HttpMethod.getMethod(tokens.get(0)));
            requestBuilder.setURI(tokens.get(1));
            requestBuilder.setHttpVersion(HttpVersion.getCompatibleVersion(tokens.get(2)));
        } catch (BadHttpMethodException e) {
            throw new HttpParsingException(HttpStatus.NOT_IMPLEMENTED);
        } catch (BadHttpVersionException e) {
            throw new HttpParsingException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    private void parseHeaderLines(BufferedReader bufferedReader, HttpRequest.HttpRequestBuilder requestBuilder) throws IOException, HttpParsingException {
        int numOfReadBytes = 0;
        int _byte;
        StringBuilder sb = new StringBuilder();
        HttpHeaders headers = new HttpHeaders();
        boolean beforeColon = true;
        String key = null;
        boolean flagCRLFExists = false;
        while ((_byte = bufferedReader.read()) != -1) {
            numOfReadBytes++;
            if (numOfReadBytes > configuration.getMaxHeadersSize()) {
                throw new HttpParsingException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
            }
            if (beforeColon && _byte == SP) {
                throw new HttpParsingException(HttpStatus.BAD_REQUEST);
            }
            if (_byte == COLON) {
                beforeColon = false;
                key = sb.toString();
                sb.setLength(0);
                continue;
            }
            if (_byte == CR && flagCRLFExists) {
                _byte = bufferedReader.read();
                if (_byte != LF) {
                    throw new HttpParsingException(HttpStatus.BAD_REQUEST);
                }
                break;
            }
            if (_byte != CR) {
                sb.append((char) _byte);
                flagCRLFExists = false;
                continue;
            }
            _byte = bufferedReader.read();
            if (_byte != LF) {
                throw new HttpParsingException(HttpStatus.BAD_REQUEST);
            }
            flagCRLFExists = true;
            headers.add(key, sb.toString().trim());
            sb.setLength(0);
            beforeColon = true;
        }
        String contentLength = headers.getFirst("Content-Length");
        if (contentLength == null) {
            this.contentLength = 0;
        } else {
            try {
                this.contentLength = Integer.parseInt(contentLength);
            } catch (Exception e) {
                throw new HttpParsingException(HttpStatus.BAD_REQUEST);
            }
        }
        requestBuilder.setHttpHeaders(headers);
    }

    private void parseEntityBody(BufferedReader bufferedReader, HttpRequest.HttpRequestBuilder requestBuilder) throws IOException {
        int _byte;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            _byte = bufferedReader.read();
            sb.append((char) _byte);
        }
        requestBuilder.setBody(sb.toString());
    }
}
