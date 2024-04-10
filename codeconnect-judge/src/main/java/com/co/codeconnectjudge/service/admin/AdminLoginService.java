package com.co.codeconnectjudge.service.admin;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.model.dto.RegisterUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
public interface AdminLoginService {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult register(RegisterUser user);
}
