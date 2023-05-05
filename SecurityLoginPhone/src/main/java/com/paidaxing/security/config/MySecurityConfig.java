package com.paidaxing.security.config;

import com.paidaxing.security.filter.JwtAuthenticationTokenFilter;
import com.paidaxing.security.handler.MyAuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {


    //自定义的过滤器
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    MyAuthenticationEntryPointImpl myAuthenticationEntryPoint;
    //添加自定义的短信登录适配器
    @Autowired
    SmsSecurityConfigurerAdapter smsSecurityConfigurerAdapter;
    //加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 对于登录接口 运行匿名访问
                .authorizeRequests()
                .antMatchers("/login", "user/sms/login", "/user/sms/sendCode").anonymous()
                // 除上面外的所有请求全部要鉴权认证
                .anyRequest().authenticated()
                .and().apply(smsSecurityConfigurerAdapter);

        // 添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理器
        http.exceptionHandling()
                // 配置认证失败处理器
                .authenticationEntryPoint(myAuthenticationEntryPoint);
        // 允许跨域
        http.cors();

    }
}
