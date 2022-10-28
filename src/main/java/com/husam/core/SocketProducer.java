package com.husam.core;

import com.husam.config.Configuration;
import com.husam.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketProducer implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SocketProducer.class);
    private int port;
    private String webRoot;
    private BlockingQueue<Socket> buffer;

    private ServerSocket serverSocket;
    private Configuration configuration;

    public SocketProducer(int port, String webRoot, BlockingQueue<Socket> buffer) throws IOException {
        this.configuration = ConfigurationManager.getInstance().getCurrentConfiguration();
        this.port = port;
        this.webRoot = webRoot;
        this.buffer = buffer;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(configuration.getTimeoutWaitingRequest());
                buffer.put(socket);
            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket: ", e);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with putting socket in buffer: ", e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
