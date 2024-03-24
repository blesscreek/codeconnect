package com.co.codeconnectjudge.service.impl;

import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
