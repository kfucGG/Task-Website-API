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
public class LogPersonServiceAspect {

    @Pointcut("execution( * ru.kolomiec.taskspring.services.PersonServiceImpl.*(..)))")
    public void personServiceMethods(){};
    @Around("@target(LogPersonService) && personServiceMethods()")
    public Object logPersonServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(joinPoint.getSignature().getName() + " " + Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }
}
