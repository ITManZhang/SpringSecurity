package com.paidaxing.security.po;

import java.time.LocalDateTime;
import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * ?ƶ????û??(User)表实体类
 *
 * @author makejava
 * @since 2023-05-05 18:12:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User  {
    //用户id@TableId
    private Long id;

    //用户名
    private String username;
    //用户密码
    private String password;
    //用户昵称
    private String nickname;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //性别
    private Integer gender;
    //生日
    private Date birth;
    //城市
    private String city;
    //状态
    private Integer status;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;



}
