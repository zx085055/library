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

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.tgfc.library.controller..*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("START_TARGET : " + joinPoint.getTarget());
        StringBuilder rs = new StringBuilder();
        String className = null;
        int index = 1;
        Object[] args = joinPoint.getArgs();
        for (Object info : args) {
            // 獲取對象類型
            className = info.getClass().getName();
            className = className.substring(className.lastIndexOf(".") + 1);
            rs.append("[參數").append(index).append("，類型：").append(className).append("，值：");
            // 獲取對象的所有方法
            Method[] methods = info.getClass().getDeclaredMethods();
            // 遍歷方法，判斷get方法
            for (Method method : methods) {
                String methodName = method.getName();
                //System.out.println(methodName);
                // 判斷是不是get方法
                if (Integer.toString(methodName.indexOf("get")).equals("-1")) {// 不是get方法
                    continue;// 不處理
                }
                Object rsValue = null;
                try {
                    // 調用get方法，獲取返回值
                    rsValue = method.invoke(info);
                    if (rsValue == null) {// 沒有返回值
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                // 將值加入內容中
                rs.append("(").append(methodName).append(" : ").append(rsValue).append(")");
            }
            rs.append("]");
            index++;
        }
        logger.info("START_ARGS : " + rs.toString());
    }

    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(JoinPoint joinPoint, BaseResponse ret) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("BASE_RESPONSE : " + ret.toString());

    }

}