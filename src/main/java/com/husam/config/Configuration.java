package com.husam.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
    private int maxUriSize;
    private int maxVersionSize;
    private int maxMethodSize;
    private int maxHeadersSize;
    private int queueSize;
    private int numberOfConsumers;
    private int timeoutWaitingRequest;
    private int port;
    private String webRoot;

    public Configuration() {
    }

    public int getMaxUriSize() {
        return maxUriSize;
    }

    public int getMaxVersionSize() {
        return maxVersionSize;
    }

    public int getMaxMethodSize() {
        return maxMethodSize;
    }

    public int getMaxHeadersSize() {
        return maxHeadersSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getNumberOfConsumers() {
        return numberOfConsumers;
    }

    public int getTimeoutWaitingRequest() {
        return timeoutWaitingRequest;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public int getPort() {
        return port;
    }
}