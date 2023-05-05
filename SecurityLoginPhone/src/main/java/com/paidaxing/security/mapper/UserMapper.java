package com.paidaxing.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paidaxing.security.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * ?ƶ????û??(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-05 19:42:12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
