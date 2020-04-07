package com.gp.webdriverapi.common.aspect;

import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.common.utils.CryptoUtils;
import com.gp.webdriverapi.common.utils.ParseSystemUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Aim-Trick
 * @date 2020/4/3
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        boolean classAnnotationPresent = methodParameter.getContainingClass().isAnnotationPresent(Crypto.class);
        boolean methodAnnotationPresent = Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(Crypto.class);
        if (classAnnotationPresent) {
            System.err.println("=========support=========");
            return (methodParameter
                    .getContainingClass()
                    .getAnnotation(Crypto.class))
                    .encrypt();
        } else if (methodAnnotationPresent) {
            return (methodParameter
                    .getMethod()
                    .getAnnotation(Crypto.class))
                    .encrypt();
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {
            R result = (R) o;
            Object data = result.getData();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(data);
            String encrypt = CryptoUtils.encrypt(jsonString);
            return result.setData(encrypt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
