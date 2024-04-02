package com.co.codeconnectjudge.service.impl;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.common.result.ResultStatus;
import com.co.codeconnectjudge.manager.LoginManager;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.model.vo.RegisterUser;
import com.co.codeconnectjudge.service.LoginService;
import com.co.codeconnectjudge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.co.codeconnectjudge.constant.RedisConstants.LOGIN_CODE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginManager loginManager;

    @Override
    public ResponseResult login(User user) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "登录成功",loginManager.login(user));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }

    }

    @Override
    public ResponseResult logout() {
        loginManager.logout();
        return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "退出成功");
    }

    @Override
    public ResponseResult register(RegisterUser registerUser)  {
        try {
            loginManager.register(registerUser);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "注册成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

}
