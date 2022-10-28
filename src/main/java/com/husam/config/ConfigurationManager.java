package com.husam.config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConfigurationManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);
    private Configuration configuration;
    private static volatile ConfigurationManager instance;
    private static Object mutex = new Object();

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null)
                    instance = new ConfigurationManager();
            }
        }
        return instance;
    }

    public void loadConfigurationFile(String filePath) {
        try {
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.configuration = (Configuration) jaxbUnmarshaller.unmarshal(file);

        } catch (Exception e) {
            LOGGER.error("Error while unmarshalling, ", e);
        }
    }

    public Configuration getCurrentConfiguration() {
        if (this.configuration == null) {
            LOGGER.error("No Current Configuration Set.");
            return null;
        }
        return configuration;
    }
}
