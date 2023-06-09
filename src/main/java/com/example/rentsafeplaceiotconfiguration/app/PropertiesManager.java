package com.example.rentsafeplaceiotconfiguration.app;

import java.io.*;
import java.util.Properties;

public class PropertiesManager {
    private static final Properties properties = new Properties();

    public static void save(String key, String value) {
        try(OutputStream outputStream = new FileOutputStream("properties.properties")){
            properties.setProperty(key, value);
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String key) {
        try {
            properties.load(new FileInputStream("properties.properties"));
            return properties.getProperty(key) == null ? "" : properties.getProperty(key);
        } catch (Exception exception) {
            return "";
        }
    }
}
