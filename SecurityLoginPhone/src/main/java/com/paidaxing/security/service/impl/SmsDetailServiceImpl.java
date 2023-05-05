package com.paidaxing.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paidaxing.security.mapper.UserMapper;
import com.paidaxing.security.po.LoginUser;
import com.paidaxing.security.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Service
public class SmsDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone);
        User user = userMapper.selectOne(wrapper);
        LoginUser loginUser = null;
        String pass = passwordEncoder.encode("123456");
        if (Objects.isNull(user)){
            User login = new User();
            login.setUsername("新用户");
            login.setPassword(pass);
            login.setPhone(phone);
            login.setCreateTime(LocalDateTime.now());
            userMapper.insert(login);
            loginUser = new LoginUser(login, Arrays.asList("test", "admin"));
        }else {
            //TODO 查询数据库权限信息
            loginUser = new LoginUser(user, Arrays.asList("test", "admin"));
        }

        return loginUser;
    }
}
