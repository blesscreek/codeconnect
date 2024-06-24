package com.co.backend.service.admin.impl;

import com.co.backend.model.po.User;
import com.co.backend.service.admin.AdminLoginService;
import com.co.backend.common.result.ResponseResult;
import com.co.common.exception.StatusFailException;
import com.co.backend.common.result.ResultStatus;
import com.co.backend.manager.admin.AdminLoginManager;
import com.co.backend.model.dto.RegisterUserDTO;
import com.co.common.exception.StatusForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminLoginServiceImpl implements AdminLoginService {

    @Autowired
    private AdminLoginManager adminLoginManager;

    @Override
    public ResponseResult login(User user) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "登录成功", adminLoginManager.login(user));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }

    }

    @Override
    public ResponseResult logout() {
        adminLoginManager.logout();
        return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "退出成功");
    }

    @Override
    public ResponseResult register(RegisterUserDTO registerUserDTO)  {
        try {
            adminLoginManager.register(registerUserDTO);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "注册成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult refreshToken(String refreshToken) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "刷新token成功",adminLoginManager.refreshToken(refreshToken));
        }catch (StatusForbiddenException e) {
            return new ResponseResult(ResultStatus.FORBIDDEN.getStatus(),e.getMessage());
        }
    }

}
