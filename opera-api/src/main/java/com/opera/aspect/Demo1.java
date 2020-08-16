package com.opera.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志切面类
 */
@Aspect
@Component
@Order(5)
public class Demo1 {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private ThreadLocal<Long> startTime=new ThreadLocal<>();


    @Pointcut("execution(public * com.opera.controllers.*.*(..))")
    public void webLog(){

    }

    @Before(value = "webLog()")
    public void doBefore(JoinPoint point){
        startTime.set(System.currentTimeMillis());

        logger.info("WebLogAspect.doBefore............");
        ServletRequestAttributes attributes=
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request=attributes.getRequest();
//        logger.info("IP : "+request.getRemoteAddr());
//        logger.info("URL:" + request.getRequestURL().toString());
//        logger.info("HTTP_METHOD:" +request.getMethod());
//        logger.info("CLASS_NAME : " + point.getSignature().getDeclaringTypeName()+"."+point.getSignature().getName());
//        logger.info("ARGS : " + Arrays.toString(point.getArgs()));
    }


    @AfterReturning(value = "webLog()",returning = "ret")
    public void doAfterReturning(Object ret){
        logger.info("WebLogAspect.doAfterReturning.............");
        logger.info("Resp: " + ret);
        logger.info("Spend Time : " + (System.currentTimeMillis()-startTime.get()));
    }
}
