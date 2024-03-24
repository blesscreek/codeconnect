package com.co.codeconnectjudge.controller;

import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.model.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-23 11:09
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    UserMapper userMapper;
    @RequestMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id) {
        User user = userMapper.selectById(id);
        return user;
    }
}
