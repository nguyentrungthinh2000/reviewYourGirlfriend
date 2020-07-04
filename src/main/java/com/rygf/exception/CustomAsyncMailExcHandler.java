package com.rygf.exception;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class CustomAsyncMailExcHandler implements AsyncUncaughtExceptionHandler {
    
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        log.error("Has Mail error : {}", throwable.getMessage());
        log.error("At method : {}", method.getName());
    }
}
