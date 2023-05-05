package com.paidaxing.security.config;

import com.paidaxing.security.filter.SmsAuthenticationFilter;
import com.paidaxing.security.handler.MyAuthenticationSuccessHandler;
import com.paidaxing.security.provide.SmsAuthenticationProvider;
import com.paidaxing.security.service.impl.SmsDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 编写自定义的短信登录适配器
 */
@Component
public class SmsSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private SmsDetailServiceImpl smsDetailService;
    
    //重写configure方法
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //为自定义SmsAuthenticationFilter的过滤器设置认证管理对象和认证成功处理器
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());

        //为自定义的认证器设置认证方式
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider(redisTemplate, smsDetailService);
        //将认证器设置并且将自定义的过滤器添加到默认的前面
        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterBefore(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
