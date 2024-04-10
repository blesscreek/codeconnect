package com.co.codeconnectjudge.service.admin.impl;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.common.result.ResultStatus;
import com.co.codeconnectjudge.manager.admin.AdminLoginManager;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.model.dto.RegisterUser;
import com.co.codeconnectjudge.service.admin.AdminLoginService;
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
    public ResponseResult register(RegisterUser registerUser)  {
        try {
            adminLoginManager.register(registerUser);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "注册成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

}
