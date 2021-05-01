package com.hgcw.wiki.config;


import com.hgcw.wiki.interceptor.LogInterceptor;
import com.hgcw.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 接口拦截器进行校验拦截接口
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    //.addPathPatterns("/**")拦截所有接口
    //    excludePathPatterns（）开放的接口
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/euser/login",
                        "/api/dimSelect",
                        "/euser/dimSelect/**",
                        "/categoty/dimSelect",
                        "/doc/selectList",
                        "/doc/vote/**",
                        "/categoty/selectList"
                );
//
//        registry.addInterceptor(actionInterceptor)
//                .addPathPatterns(
//                        "/*/save",
//                        "/*/delete/**",
//                        "/*/reset-password");
    }
}
