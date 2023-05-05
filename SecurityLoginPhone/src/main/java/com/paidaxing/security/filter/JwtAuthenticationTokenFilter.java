package com.paidaxing.security.filter;

import com.paidaxing.security.po.LoginUser;
import com.paidaxing.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt 过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //从请求头获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //空直接放行，不需要处理的
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userId = "";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }
        //从redis中获取token
        String redisToken  = (String) redisTemplate.opsForValue().get(userId);
        if(StringUtils.isEmpty(redisToken) || (!redisToken.equals(token))){
            throw new AccountExpiredException("token过期，请重新登录");
        }
        //从redis中获取用户信息
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(userId);

        if(Objects.isNull(loginUser)){
            throw new AccountExpiredException("请重新登录");
        }
        //将用户信息存入UsernamePasswordAuthenticationToken中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
