package com.paidaxing.security.handler;

import com.alibaba.fastjson.JSON;
import com.paidaxing.security.common.BaseResult;
import com.paidaxing.security.po.LoginUser;
import com.paidaxing.security.utils.JwtUtil;
import com.paidaxing.security.utils.WebUtils;
import com.paidaxing.security.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    static RedisTemplate redisTemplate;


    // 解决 @Component 下 @Autowired 注入为null的情况
    @Autowired
    public void setStringRedisTemplate(RedisTemplate redisTemplate) {
        MyAuthenticationSuccessHandler.redisTemplate = redisTemplate;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(id);
        this.redisTemplate.opsForValue().set(id,token,1, TimeUnit.HOURS);
        this.redisTemplate.opsForValue().set(id,loginUser);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(loginUser.getUser(),userVo);
        userVo.setToken(token);

        String jsonString = JSON.toJSONString(BaseResult.ok(userVo));
        WebUtils.renderString(httpServletResponse,jsonString);
    }
}
