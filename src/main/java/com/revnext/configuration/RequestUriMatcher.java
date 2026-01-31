package com.revnext.configuration;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Optional;

@Component
public class RequestUriMatcher {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public boolean matches(@NonNull RequestAuthorizationContext context, @NonNull RequestMappingInfo mappingInfo) {
        String requestUri = new UrlPathHelper().getPathWithinApplication(context.getRequest());
        System.out.println(requestUri);
        if (mappingInfo.getPathPatternsCondition() != null) {
            for (PathPattern pattern : mappingInfo.getPathPatternsCondition().getPatterns()) {
                if (patternMatches(requestUri, pattern.getPatternString())) {
                    return true; // Match found
                }
            }
        }

        return false; // No match
    }

    public Optional<String> getPattern(@NonNull RequestAuthorizationContext context) {
        return requestMappingHandlerMapping.getHandlerMethods().keySet().stream()
                .filter(handlerMethod -> matches(context, handlerMethod))
                .map(handlerMethod -> getPattern(context, handlerMethod))
                .findFirst()
                .orElseThrow();

    }

    public Optional<String> getPattern(@NonNull RequestAuthorizationContext context, @NonNull RequestMappingInfo mappingInfo) {
        String requestUri = new UrlPathHelper().getPathWithinApplication(context.getRequest());
        if (mappingInfo.getPathPatternsCondition() != null) {
            for (PathPattern pattern : mappingInfo.getPathPatternsCondition().getPatterns()) {
                if (patternMatches(requestUri, pattern.getPatternString())) {
                    return Optional.of(pattern.getPatternString()); // Match found
                }
            }
        }

        return Optional.empty();
    }

    private boolean patternMatches(@NonNull String uri, @NonNull String pattern) {
        return new org.springframework.util.AntPathMatcher().match(pattern, uri);
    }
}
