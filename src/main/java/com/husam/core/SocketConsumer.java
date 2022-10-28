package com.husam.core;

import com.husam.CGI.CGI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

import com.husam.http.*;
import com.husam.httpParser.HttpParser;
import com.husam.httpParser.HttpParsingException;
import com.husam.http.HttpResponse.HttpResponseBuilder;

public class SocketConsumer implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(SocketConsumer.class);
    private BlockingQueue<Socket> buffer;

    public SocketConsumer(BlockingQueue<Socket> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try (
                    Socket socket = buffer.take();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
            ) {
                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());

                HttpParser parser = new HttpParser();
                HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
                httpResponseBuilder.setHttpVersion(HttpVersion.HTTP_1_1);
                HttpHeaders headers = new HttpHeaders();
                try {
                    HttpRequest request = parser.parseHttpRequest(inputStream);
                    httpResponseBuilder.setHttpStatus(HttpStatus.HTTP_OK);
                    CGI cgi = new CGI(request);
                    httpResponseBuilder.setBody(cgi.getResource());
                } catch (HttpParsingException e) {
                    httpResponseBuilder.setHttpStatus(e.getHttpStatus());
                }
                httpResponseBuilder.setHttpHeaders(headers);
                HttpResponse response = httpResponseBuilder.build();
                outputStream.write(response.build());
                outputStream.flush();

                LOGGER.info(" * Connection Done: " + socket.getInetAddress());
            } catch (InterruptedException e) {
                LOGGER.error("Problem with taking socket from buffer: ", e);
            } catch (SocketTimeoutException e) {
                LOGGER.warn("Socket timeout");
            } catch (IOException e) {
                LOGGER.error("Problem with IO/stream: ", e);
            }
        }
    }
}