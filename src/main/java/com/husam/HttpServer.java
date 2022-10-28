package com.husam;


import com.husam.config.Configuration;
import com.husam.config.ConfigurationManager;
import com.husam.core.SocketConsumer;
import com.husam.core.SocketProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        configurationManager.loadConfigurationFile("src/main/resources/serverConf.xml");
        Configuration configuration = configurationManager.getCurrentConfiguration();

        BlockingQueue<Socket> buffer = new LinkedBlockingQueue<>(configuration.getQueueSize());
        LOGGER.info("Start the program");
        try {
            SocketProducer socketProducer = new SocketProducer(configuration.getPort(), configuration.getWebRoot(), buffer);
            Thread producer = new Thread(socketProducer);
            producer.start();
            for(int i = 0; i < configuration.getNumberOfConsumers(); i++){
                Thread consumer = new Thread(new SocketConsumer(buffer));
                consumer.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
