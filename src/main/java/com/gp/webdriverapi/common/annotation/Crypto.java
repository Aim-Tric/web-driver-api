package com.gp.webdriverapi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密解密相关注解
 *
 * @author Devonte
 * @date 2020/04/05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Crypto {

    boolean encrypt() default true;

    boolean decrypt() default true;

}
