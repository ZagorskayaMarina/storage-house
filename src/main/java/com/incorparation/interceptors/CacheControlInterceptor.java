package com.incorparation.interceptors;

import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheControlInterceptor implements HandlerInterceptor {
    private static final List<String> URL_WITH_CACHE = Arrays.asList("/storage/api/products/categories");
    private static final List<String> URL_NO_CACHE = Arrays.asList("/storage/api/products/test");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AntPathMatcher matcher = new AntPathMatcher();

        if (request.getMethod().equals("GET")) {
            URL_WITH_CACHE.forEach(urlWithCache -> {
                boolean isWithCache = matcher.match(urlWithCache, request.getRequestURI());

                if (isWithCache) {
                    response.addHeader("Cache-Control", CacheControl.maxAge(5, TimeUnit.HOURS).toString());
                } else {
                    response.addHeader("Cache-Control","no-cache");
                }
            });
        } else {
            if (!request.getMethod().equals("OPTIONS")) {
                response.addHeader("Cache-Control", "no-cache");
            }
        }

        return true;
        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
