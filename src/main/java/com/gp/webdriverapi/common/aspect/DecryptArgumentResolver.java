package com.gp.webdriverapi.common.aspect;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vent
 * @date 2020/04/05
 */
public class DecryptArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object obj = methodParameter.getParameterType().newInstance();
        Iterator<String> parameterNames = nativeWebRequest.getParameterNames();
        while (parameterNames.hasNext()) {
            String key = parameterNames.next();
            String parameter = nativeWebRequest.getParameter(key);
            if (parameter != null) {
                BeanUtils.setProperty(obj, key, parameter);
            }
        }
        return obj;
    }
}
