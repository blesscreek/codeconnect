package com.co.codeconnectjudge.dao.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.dao.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-02 21:46
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账户名找用户数量
     * @param account
     * @return
     */
    @Override
    public Long countUserByAccount(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long count = userMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 根据账户名查用户id
     * @param account
     * @return
     */
    @Override
    public Long selectUserIdByAccount(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",account);
        User user = userMapper.selectOne(queryWrapper);
        return user.getId();
    }
}
