package com.incorparation.controller;

import com.incorparation.exception.ApplicationError;
import com.incorparation.exception.CommonException;
import com.incorparation.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@Component
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
    public static final Logger LOGGER = LogManager.getLogger(ExceptionHandlerControllerAdvice.class);
    private final PropertiesLoader propertiesLoader;

    @Autowired
    public ExceptionHandlerControllerAdvice(PropertiesLoader propertiesLoader) {
        this.propertiesLoader = propertiesLoader;
    }

    @ExceptionHandler(value = {CommonException.class})
    public ResponseEntity handleGenericException(Exception exception, WebRequest request) {
        LOGGER.error(String.format("Error occurred while endpoint: [%s] is canceled", ((ServletWebRequest)request).getRequest().getRequestURI()),exception);
        var applicationError = buildApplicationError(exception.getMessage());

        return ResponseEntity.status(applicationError.getHttpErrorCode())
                .body(applicationError);
    }

    private ApplicationError buildApplicationError(String messageKey) {
        var errors = propertiesLoader.buildError(messageKey);

        var errorMessage = Optional.ofNullable(errors.get(0))
                .map(String::trim)
                .orElse(messageKey);

        var errorCode = Optional.ofNullable(errors.get(1))
                .map(Integer::valueOf)
                .orElse(HttpStatus.BAD_REQUEST.value());

        return new ApplicationError(errorMessage, errorCode);
    }
}
