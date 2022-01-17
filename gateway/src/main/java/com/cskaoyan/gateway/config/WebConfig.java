package com.cskaoyan.gateway.config;

import com.mall.user.intercepter.TokenIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
    create by ciggar
 * create-date: 2019/7/22-19:01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public TokenIntercepter tokenIntercepter(){
        return new TokenIntercepter();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenIntercepter())
                .addPathPatterns("/shopping/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/cashier/**")
                .excludePathPatterns("/error");
    }
}
