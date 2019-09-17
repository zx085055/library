package com.tgfc.library.aop;

import com.tgfc.library.response.BaseResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.tgfc.library.controller..*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws Exception {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("START_TARGET : " + joinPoint.getTarget());
        StringBuilder rs = new StringBuilder();
        String className = null;
        Object[] args = joinPoint.getArgs();
        for (Object info : args) {
            className = info.getClass().getName();// 獲取對象類型
            className = className.substring(className.lastIndexOf(".") + 1);
            rs.append("[參數").append(Arrays.toString(args).indexOf(info.toString())).append("，類型：").append(className).append("，值：");
            Method[] methods = info.getClass().getDeclaredMethods();// 獲取對象的所有方法
            for (Method method : methods) {// 遍歷方法，判斷get方法
                Parameter[] parameters = method.getParameters();
                if (parameters.length > 0) {
                    for (Method methodGet : methods) {// 遍歷方法，判斷get方法
                        String methodGetName = methodGet.getName().toUpperCase();
                        if (methodGetName.contains("GET") && methodGetName.contains(parameters[0].getName().toUpperCase())) {// 判斷是不是get方法，是get方法
                            Object rsValue = null;
                            rsValue = methodGet.invoke(info);// 調用get方法，獲取返回值
                            if (rsValue == null) {// 沒有返回值
                                continue;
                            }
                            rs.append("(").append(parameters[0].getName()).append(" : ").append(rsValue).append(")");// 將值加入內容中
                        }
                    }
                }
            }
            rs.append("]");
        }
        logger.info("START_ARGS : " + rs.toString());
    }

    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(JoinPoint joinPoint, BaseResponse ret) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("BASE_RESPONSE : " + ret.toString());

    }

}