package com.revnext.configuration;


import com.auth0.jwt.algorithms.Algorithm;
import com.revnext.handler.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executors;

@Configuration
public class AppConfig implements AsyncConfigurer {

    @Value("${token.duration.days}")
    private long durationDays;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Bean
    Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}