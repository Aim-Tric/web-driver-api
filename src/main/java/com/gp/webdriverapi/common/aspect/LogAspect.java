package com.gp.webdriverapi.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面
 * 为每个API添加访问日志
 *
 * @author vent
 * @date 2020/03/15
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.gp.webdriverapi.system.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求相关参数
        log.info("============= Start =============");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args   : {}", joinPoint.getArgs());
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        long begin = System.nanoTime();
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error(throwable.getMessage());
        }
        long end = System.nanoTime();
        log.info(" {} 执行完毕，耗时: [{}ms]", proceedingJoinPoint.getSignature(), (end - begin) / 1000000);
        return proceed;
    }

    @After("log()")
    public void doAfter() throws Throwable {
        log.info("============= End =============");
        // 每个请求之间空一行
        log.info("");
    }

}
