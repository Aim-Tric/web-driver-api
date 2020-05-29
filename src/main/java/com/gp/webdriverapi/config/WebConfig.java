package com.gp.webdriverapi.config;

import com.gp.webdriverapi.common.aspect.DecryptArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 参数处理器配置
 *
 * @author Devonte
 * @date 2020/4/19
 */
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(decryptArgumentResolver());
    }

    @Bean
    public DecryptArgumentResolver decryptArgumentResolver() {
        return new DecryptArgumentResolver();
    }
}
