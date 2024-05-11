package com.co.codeconnectjudge.service.overwrite;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.co.codeconnectjudge.mapper.MenuMapper;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
        queryWrapper.eq(User::getAccount,s);
        User user = userMapper.selectOne(queryWrapper);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        List<String> list =
                menuMapper.selectPermsByUserId(user.getId());
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        //封装成userDetails对象返回
        return new LoginUser(user, list);
    }
}