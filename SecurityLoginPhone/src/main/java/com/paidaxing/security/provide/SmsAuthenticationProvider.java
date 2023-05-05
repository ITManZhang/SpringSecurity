package com.paidaxing.security.provide;

import com.paidaxing.security.dto.SmsCodeAuthenticationToken;
import com.paidaxing.security.service.impl.SmsDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private SmsDetailServiceImpl smsDetailService;

    public SmsAuthenticationProvider(RedisTemplate redisTemplate, SmsDetailServiceImpl smsDetailService) {
        this.redisTemplate = redisTemplate;
        this.smsDetailService = smsDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //从认证中获取手机号和验证码
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        Object principal = authenticationToken.getPrincipal();
        String phone = "";
        if (principal instanceof String){
            phone = (String) principal;
        }
        String inputCode = (String) authentication.getCredentials();
        //1.从redis中获取验证码,key为login:加手机号；进行检验
        String redisCode = (String) redisTemplate.opsForValue().get(phone);
        //对验证码过期判断
        if (!StringUtils.hasText(redisCode)) {
            throw new BadCredentialsException("验证码已经过期或尚未发送，请重新发送验证码");
        }
        //对验证码比对处理
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        redisTemplate.delete(phone);
        UserDetails loginUser = smsDetailService.loadUserByUsername(phone);
        SmsCodeAuthenticationToken authenticationTokenNew =
                new SmsCodeAuthenticationToken(loginUser,inputCode,loginUser.getAuthorities());
        authenticationTokenNew.setDetails(authenticationToken.getDetails());

        return authenticationTokenNew;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
