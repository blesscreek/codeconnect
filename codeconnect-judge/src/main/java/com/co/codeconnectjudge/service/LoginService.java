package com.co.codeconnectjudge.service;

import com.co.codeconnectjudge.common.ResponseResult;
import com.co.codeconnectjudge.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
public interface LoginService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult logout();
}
