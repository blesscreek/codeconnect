package com.co.codeconnectjudge.service;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.co.codeconnectjudge.model.vo.RegisterUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult register(RegisterUser user);
}
