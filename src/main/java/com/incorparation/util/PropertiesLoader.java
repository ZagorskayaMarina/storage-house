package com.incorparation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Objects;

@Configuration
@PropertySource({"classpath:applicationError.properties"})
public class PropertiesLoader {
    private final Environment environment;

    @Autowired
    public PropertiesLoader(Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String propertyKey) {
        return environment.getProperty(propertyKey);
    }

    public List<String> buildError(String property) {
        var errorMessageKey = Objects.requireNonNullElse(
                getProperty("storageHouse.resultMessage.error." + property),
                property
        );

        var errorHttpCodeKey = Objects.requireNonNullElse(
                getProperty("storageHouse.resultHttpCode.error." + property),
                getProperty("resultCode.httpError.bad.request")
        );

        return List.of(errorMessageKey, errorHttpCodeKey);
    }
}
