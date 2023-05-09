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
    private static final List<String> NO_CACHE_URL_PATTERN = Arrays.asList("/storage/api/products/test");
    //private static final List<String> EXCLUDE_CACHE_URL_PATTERN = Arrays.asList("/storage/api/products/categories");
    private static final List<String> EXCLUDE_CACHE_URL_PATTERN = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("GET")) {
            var isURLExclude = isURLMatch(EXCLUDE_CACHE_URL_PATTERN, request.getRequestURI());
            var isURLNoCached = isURLMatch(NO_CACHE_URL_PATTERN, request.getRequestURI());

            if (isURLExclude) {
                return true;
            } else if (isURLNoCached) {
                response.addHeader("Cache-Control","no-cache");
            } else {
                response.addHeader("Cache-Control", CacheControl.maxAge(5, TimeUnit.HOURS).getHeaderValue());
            }
        }

        return true;
    }

    private boolean isURLMatch(List<String> patternList, String url) {
        AntPathMatcher matcher = new AntPathMatcher();

        return patternList.stream().anyMatch(pattern -> matcher.match(pattern, url));
    }
}
