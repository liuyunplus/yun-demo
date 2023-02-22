package com.github.liuyun.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.liuyun.mapper.UserMapper;
import com.github.liuyun.model.UserDO;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, UserDO> {

    public Boolean login(String username, String password) {
        UserDO user = this.getBaseMapper().getUser(username, password);
        return user != null;
    }

}
