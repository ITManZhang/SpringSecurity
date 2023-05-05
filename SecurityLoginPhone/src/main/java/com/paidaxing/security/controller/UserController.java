package com.paidaxing.security.controller;

import com.paidaxing.security.common.BaseResult;
import com.paidaxing.security.dto.UserDto;
import com.paidaxing.security.po.User;
import com.paidaxing.security.utils.SMSUtils;
import com.paidaxing.security.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
        return "hello security";
    }

    @PostMapping("/user/sms/sendCode")
    public BaseResult sendCode(@RequestBody UserDto userDto){
        //思路：对手机判空，将手机号与密码存入redis
        String phone = userDto.getPhone();
        if (StringUtils.hasText(phone)&& Pattern.matches("^1[3-9]\\d{9}$",phone)){
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("验证码：{}",code);
            //短信发送工具类
            //SMSUtils.sendMessage();
            redisTemplate.opsForValue().set(phone,code);
        }
        return BaseResult.ok("验证码发送成功");
    }
}
