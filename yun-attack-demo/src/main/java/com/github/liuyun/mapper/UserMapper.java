package com.github.liuyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.liuyun.model.UserDO;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select * from t_user_1 where username='${username}' and password='${password}'")
    UserDO getUser(String username, String password);

}
