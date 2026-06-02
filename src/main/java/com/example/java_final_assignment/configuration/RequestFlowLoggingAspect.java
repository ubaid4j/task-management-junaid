package com.example.java_final_assignment.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RequestFlowLoggingAspect {

    // Target tracking across all controller files
    @Pointcut("within(com.example.java_final_assignment.controllers..*)")
    public void controllerMethods() {}

    // Target tracking across all service implementations
    @Pointcut("within(com.example.java_final_assignment.service..*)")
    public void serviceMethods() {}

    @Around("controllerMethods()")
    public Object logControllerFlow(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());

        log.info("---> [CONTROLLER ENTER] Method: {} | Args: {}", methodName, args);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("<--- [CONTROLLER EXIT] Method: {} completed in {}ms", methodName, executionTime);
            return result;
        } catch (Throwable e) {
            log.error("[CONTROLLER EXCEPTION] Method: {} failed with message: {}", methodName, e.getMessage());
            throw e;
        }
    }

    @Around("serviceMethods()")
    public Object logServiceFlow(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("  ==> [SERVICE FLOW] Entering: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("  <== [SERVICE FLOW] Exiting: {}", methodName);
            return result;
        } catch (Throwable e) {
            log.error("  [SERVICE EXCEPTION] In: {} | Message: {}", methodName, e.getMessage());
            throw e;
        }
    }
}