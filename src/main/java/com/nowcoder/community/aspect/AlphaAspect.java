package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2022/7/13 21:44
 */
//@Component
//@Aspect
public class AlphaAspect {

    // 任意返回值类型 service包下所有的类中的任意方法
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    @Before("point()")
    public void before() {
        System.out.println("before");
    }

    @After("point()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("point()")
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    @AfterThrowing("point()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("point()")//环绕通知
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundBefore");
        Object proceed = joinPoint.proceed();
        System.out.println("aroundAfter");
        return proceed;
    }

}
