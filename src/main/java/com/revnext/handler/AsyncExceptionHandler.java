package com.revnext.handler;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@CommonsLog
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Unexpected asynchronous exception at : "
                + method.getDeclaringClass().getName() + "." + method.getName(), ex);
    }
}
