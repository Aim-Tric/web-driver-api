package com.gp.webdriverapi.common.aspect;

import com.gp.webdriverapi.common.annotation.Crypto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;


/**
 * @author vent
 * @date 2020/04/05
 */
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type type,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        boolean classAnnotationPresent = methodParameter.getContainingClass().isAnnotationPresent(Crypto.class);
        boolean methodAnnotationPresent = Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(Crypto.class);
        if (classAnnotationPresent) {
            return (methodParameter
                    .getContainingClass()
                    .getAnnotation(Crypto.class))
                    .decrypt();
        } else if (methodAnnotationPresent) {
            return (methodParameter
                    .getMethod()
                    .getAnnotation(Crypto.class))
                    .decrypt();
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage,
                                           MethodParameter methodParameter,
                                           Type type,
                                           Class<? extends HttpMessageConverter<?>> aClass)
            throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o,
                                HttpInputMessage httpInputMessage,
                                MethodParameter methodParameter,
                                Type type,
                                Class<? extends HttpMessageConverter<?>> aClass) {
        System.out.println("===== request after ======");
        System.out.println(o);
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o,
                                  HttpInputMessage httpInputMessage,
                                  MethodParameter methodParameter,
                                  Type type,
                                  Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
