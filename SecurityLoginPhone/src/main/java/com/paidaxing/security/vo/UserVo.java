package com.paidaxing.security.vo;

import com.paidaxing.security.po.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserVo {
    //用户id@TableId
    private Long id;
    //用户名
    private String username;
    //用户昵称
    private String nickname;
    //token
    String token;
}
