package com.co.backend.dao.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.backend.dao.user.UserEntityService;
import com.co.backend.model.po.User;
import com.co.backend.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author co
 * @since 2024-06-18
 */
@Service
public class UserEntityServiceImpl extends ServiceImpl<UserMapper, User> implements UserEntityService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账户名找用户数量
     * @param username
     * @return
     */
    @Override
    public Long countUserByUsername(String username) {
        QueryWrapper<com.co.backend.model.po.User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 根据账户名查用户id
     * @param username
     * @return
     */
    @Override
    public Long selectUserIdByUsername(String username) {
        QueryWrapper<com.co.backend.model.po.User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        com.co.backend.model.po.User user = userMapper.selectOne(queryWrapper);
        return user.getId();
    }
}
