package com.incorparation.interceptors;

import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheControlInterceptor implements HandlerInterceptor {
    private static final List<String> URL_NO_CACHE = Arrays.asList("/storage/api/products/test");
    private static final List<String> URL_EXCLUDE_CACHE = Arrays.asList("/storage/api/products/categories");
    //private static final List<String> URL_EXCLUDE_CACHE = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("GET")) {
            boolean isURLExclude = isURLMatch(URL_EXCLUDE_CACHE, request.getRequestURI());
            boolean isURLNoCached = isURLMatch(URL_NO_CACHE, request.getRequestURI());

            if (isURLExclude) {
                return true;
            } else if (isURLNoCached) {
                response.addHeader("Cache-Control","no-cache");
            } else {
                response.addHeader("Cache-Control", CacheControl.maxAge(5, TimeUnit.HOURS).toString());
            }
        }

        return true;
    }

    private boolean isURLMatch(List<String> urls, String toFind) {
        return urls.contains(toFind);
    }
}
