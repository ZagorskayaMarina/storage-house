package com.incorparation.security.filters;

import com.incorparation.annotations.Authenticate;
import com.incorparation.exception.CommonException;
import com.incorparation.service.impl.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Component
public class SecurityFilter implements Filter {
    private final HandlerMapping handlerMapping;
    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityFilter(@Qualifier("requestMappingHandlerMapping") HandlerMapping handlerMapping, AuthenticationService authenticationService) {
        this.handlerMapping = handlerMapping;
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            filterRequest(httpRequest);
            filterChain.doFilter(httpRequest, httpResponse);
        } catch (Exception ex) {
            throw new CommonException("invalid.token");
        }
    }

    private void filterRequest(HttpServletRequest request) throws Exception {
        if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
            ServletRequestPathUtils.parseAndCache(request);
        }
        var method = getMethod(request).orElse(null);
        if (HttpMethod.OPTIONS.matches(request.getMethod()) || Objects.isNull(method)) {
            return;
        }

        Authenticate annotation = method.getAnnotation(Authenticate.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        var token = getAuthToken(request);
        handleStorageVerification(token);
    }

    private Optional<Method> getMethod(HttpServletRequest request) throws Exception {
        var handlerExecutionChain = handlerMapping.getHandler(request);
        return Optional.ofNullable(handlerExecutionChain).map(HandlerExecutionChain::getHandler)
                .map(h -> {
                    if (h.getClass().isAssignableFrom(HandlerMethod.class)) {
                        return ((HandlerMethod) h).getMethod();
                    }
                    return null;
                });
    }

    private String getAuthToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");

        if (StringUtils.isBlank(authorization)) {
            throw new CommonException("invalid.token");
        }

        var authArray = authorization.split("\\s+");

        if (authArray.length < 2) {
            throw new CommonException("invalid.token");
        }

        return authArray[1];
    }

    private void handleStorageVerification(String token) {
        if(authenticationService.isStorageExist(token)) {
            return;
        } else {
            throw new CommonException("invalid.token");
        }
    }

    private void writeErrorResponse(HttpServletResponse response){

        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getOutputStream().write(("""
                    {\n
                        \"errorMessage\": \"Invalid token\"\n
                    }"""
            ).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new CommonException("Error to build", ex);
        }
    }
}