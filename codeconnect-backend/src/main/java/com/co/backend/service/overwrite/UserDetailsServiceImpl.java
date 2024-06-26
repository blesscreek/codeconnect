package com.co.backend.service.overwrite;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.mapper.MenuMapper;
import com.co.backend.mapper.UserMapper;
import com.co.backend.model.entity.LoginUser;
import com.co.backend.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author co
 * @Version 1.0
 * @Description 重写loadUserByUsername方法，实现数据库中查询用户
 * @Date 2024-03-24 22:14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,s);
        User user = userMapper.selectOne(queryWrapper);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<String> list =
                menuMapper.selectPermsByUserId(user.getId());
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        //封装成userDetails对象返回
        return new LoginUser(user, list);
    }
}