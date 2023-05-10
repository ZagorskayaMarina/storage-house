package com.incorparation.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheControlInterceptor implements HandlerInterceptor {
    private static final String GET_METHOD = "GET";
    private static final String CACHE_CONTROL = "Cache-Control";
    public static final String NO_CACHE_VALUE = "no-cache";
    @Value("${cache.control.max.age}")
    private long maxAge;

    private final List<String> noCacheUrlPattern = new ArrayList<>();
    private final List<String> excludeCacheUrlPattern = new ArrayList<>();

    public void addNoCache(String ... paths) {
        addPatterns(paths, noCacheUrlPattern);
    }

    public void addExcludeCache(String ... paths) {
        addPatterns(paths, excludeCacheUrlPattern);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        var method = request.getMethod();
        var url = request.getRequestURI();

        if (method.equals(GET_METHOD)) {
            if (!isURLMatch(noCacheUrlPattern, url) && !isURLMatch(excludeCacheUrlPattern, url)) {
                addCacheControlHeader(response, CacheControl.maxAge(maxAge, TimeUnit.MINUTES).getHeaderValue());
            } else if (isURLMatch(noCacheUrlPattern, url)) {
                addCacheControlHeader(response, NO_CACHE_VALUE);
            }
        }

        return true;
    }

    private void addPatterns(String[] paths, List<String> defaultList) {
        if (paths != null && CollectionUtils.isEmpty(defaultList)) {
            Collections.addAll(defaultList, paths);
        }
    }

    private boolean isURLMatch(List<String> patternList, String url) {
        var matcher = new AntPathMatcher();

        return patternList.stream()
                .anyMatch(pattern -> matcher.match(pattern, url));
    }

    private void addCacheControlHeader(HttpServletResponse response, String cacheValue) {
        response.addHeader(CACHE_CONTROL, cacheValue);
    }
}
