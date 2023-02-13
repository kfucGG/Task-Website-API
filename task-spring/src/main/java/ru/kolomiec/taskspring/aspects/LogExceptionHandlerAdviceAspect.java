package ru.kolomiec.taskspring.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogExceptionHandlerAdviceAspect {

    @Pointcut("execution(* ru.kolomiec.taskspring.exceptions.advices.*.*(..))")
    public void restControllerAdviceMethods() {}


    @AfterReturning(pointcut = "@target(LogExceptionHandlerAdvice) && restControllerAdviceMethods()"
                    , returning = "result")
    public void loggingExceptionHandlerAdviceMethod(JoinPoint joinPoint, ResponseEntity result) {
        log.warn(joinPoint.getSignature().getName() + result.getStatusCode());
    }
}
