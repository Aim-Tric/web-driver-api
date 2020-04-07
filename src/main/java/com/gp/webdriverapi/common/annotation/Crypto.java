package com.gp.webdriverapi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aim-Trick
 * @date 2020/4/5
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Crypto {

    boolean encrypt() default true;

    boolean decrypt() default true;

}
