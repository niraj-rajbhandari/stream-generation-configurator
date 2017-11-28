package org.niraj.stream.parser.configuration;

import org.niraj.stream.parser.helper.Helper;
import org.niraj.stream.parser.exception.PropertyNotFoundException;

import java.io.*;
import java.util.Properties;

public class ConfigReader {

    public static final String CONFIG_FILE = "config.properties";

    private Properties properties;

    private static ConfigReader configReader = null;

    ConfigReader() throws FileNotFoundException {
        Helper helper = Helper.getInstance();
        try (InputStream inputStream = new FileInputStream(helper.getAbsolutePath(CONFIG_FILE,false))) {
            this.properties = new Properties();
            if (inputStream == null) {
                throw new FileNotFoundException("Config File not found");
            }
            this.properties.load(inputStream);
        }catch(IOException e){
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public String getProperty(String key) throws PropertyNotFoundException {
        String config = this.properties.getProperty(key);
        if (config == null) {
            throw new PropertyNotFoundException("Property: " + key + " not found");
        }
        return config;
    }

    public static ConfigReader getInstance() throws FileNotFoundException {
        if (configReader == null) {
            configReader = new ConfigReader();
        }
        return configReader;
    }

}
