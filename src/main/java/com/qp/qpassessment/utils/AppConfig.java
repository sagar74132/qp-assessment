package com.qp.qpassessment.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This class is used to get the properties from the properties file
 */
@Configuration
public class AppConfig {

    private final Environment environment;

    @Autowired
    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String key, Object... args) {
        if (null == args || 0 == args.length) {
            return environment.getProperty(key);
        } else {
            String msg = environment.getProperty(key);
            if (msg == null) {
                throw new RuntimeException("Property not found: " + key);
            } else {
                try {
                    return String.format(msg, args);
                } catch (Exception e) {
                    throw new RuntimeException("Error formatting property: " + key, e);
                }
            }
        }
    }
}
