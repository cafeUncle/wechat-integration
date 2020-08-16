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
 *
 * https://blog.csdn.net/u010173095/article/details/79549882
 */
@Aspect
@Component
@Order(1)
public class Demo1 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<Long> startTime=new ThreadLocal<>();

    /**
     * .. controller包及其子包
     *
     * 修饰 返回 包名.类名.方法名(参数)，支持 Prefix*Suffix 如 Base*Service 的模式
     */
    @Pointcut("execution(public * com.opera.controllers..*.*.*(..))")
    public void webLog(){

    }

    @Before(value = "webLog()")
    public void doBefore(JoinPoint point){
        startTime.set(System.currentTimeMillis());
        System.out.println(122312312312L);
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


    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret){
        logger.info("WebLogAspect.doAfterReturning.............");
        logger.info("Resp: " + ret);
        logger.info("Spend Time : " + (System.currentTimeMillis()-startTime.get()));
    }
}
