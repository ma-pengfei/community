package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 * @date 2022/7/11 21:25
 */
@Target(ElementType.METHOD)//方法上可用
@Retention(RetentionPolicy.RUNTIME)//运行时有效
public @interface LoginRequired {


}
