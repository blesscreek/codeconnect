package com.co.backend.controller.admin;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.po.User;
import com.co.backend.model.dto.RegisterUser;
import com.co.backend.service.admin.AdminLoginService;
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
@RequestMapping("/user")
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        return adminLoginService.login(user);
    }

    @RequestMapping("/logout")
    public ResponseResult logout() {
        return adminLoginService.logout();
    }

    @RequestMapping("/register")
    public ResponseResult register(@RequestBody RegisterUser registerUser) {
        return adminLoginService.register(registerUser);
    }

    @RequestMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "ID", required = true)
    @ApiOperation("获取用户接口")
    public String getUser(@PathVariable("id") String id) {
        return "hello";
    }
}
