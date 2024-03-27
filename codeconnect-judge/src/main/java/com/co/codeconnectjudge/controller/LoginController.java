package com.co.codeconnectjudge.controller;

import com.co.codeconnectjudge.common.ResponseResult;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-23 11:09
 */
@Slf4j
@RestController
@Api(tags = "用户登录接口")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        return loginService.login(user);
    }

    @RequestMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @RequestMapping("/user/{id}")
    @ApiImplicitParam(name = "id", value = "ID", required = true)
    @ApiOperation("获取用户接口")
    public String getUser(@PathVariable("id") String id) {
        return "hello";
    }
}
