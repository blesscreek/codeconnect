package com.co.backend.service.admin;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.po.User;
import com.co.backend.model.dto.RegisterUser;

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

    ResponseResult refreshToken(String refreshToken);
}
