package com.tgfc.library.aop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tgfc.library.response.BaseResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.tgfc.library.controller..*.*(..))")
    public void pointcut() {
    }

    Gson gson = new GsonBuilder().create();

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws Exception {
        String requestMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("request :" + joinPoint.getTarget() + " , request type :"+ requestMethod);
        Object[] args = joinPoint.getArgs();
        logger.info("args : " + gson.toJson(args));
    }

    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(JoinPoint joinPoint, BaseResponse ret) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("response : " + gson.toJson(ret));
    }

}