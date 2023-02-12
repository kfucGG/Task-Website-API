package ru.kolomiec.taskspring.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogTaskServiceAspect {

    @Pointcut("execution (* ru.kolomiec.taskspring.services.TaskServiceImpl.*(..))")
    public void taskServiceMethods(){};

    @Around("@target(LogTaskService) && taskServiceMethods()")
    public Object logTaskServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(joinPoint.getSignature().getName() + " " + Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }
}
