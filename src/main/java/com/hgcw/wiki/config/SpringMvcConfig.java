package com.hgcw.wiki.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 进行校验拦截接口
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Resource
//    private LogInterceptor logInterceptor;

    //.addPathPatterns("/**")拦截所有接口
    //    excludePathPatterns（）开放的接口
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                );
//
//        registry.addInterceptor(actionInterceptor)
//                .addPathPatterns(
//                        "/*/save",
//                        "/*/delete/**",
//                        "/*/reset-password");
    }
}
