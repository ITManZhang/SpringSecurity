package com.paidaxing.security.dto;

import com.paidaxing.security.po.User;
import lombok.Data;

@Data
public class UserDto extends User {
    String code;
}
