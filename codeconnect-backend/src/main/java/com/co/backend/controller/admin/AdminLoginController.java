package com.co.backend.controller.admin;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.dto.RegisterUserDTO;
import com.co.backend.model.po.User;
import com.co.backend.service.admin.AdminLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-23 11:09
 */
@Slf4j
@RestController
@Api(tags = "用户登录接口")
@RequestMapping
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
    public ResponseResult register(@RequestBody RegisterUserDTO registerUserDTO) {
        return adminLoginService.register(registerUserDTO);
    }

    @PutMapping("/refreshToken")
    @ApiOperation("刷新token")
    public ResponseResult refreshToken(@NotBlank(message = "refreshToken不能为空") @RequestHeader("refreshToken") String refreshToken) {
        return adminLoginService.refreshToken(refreshToken);
    }

}
