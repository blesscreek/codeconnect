package com.co.backend.dao.user;

import com.co.backend.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author co
 * @since 2024-06-18
 */
public interface UserEntityService extends IService<User> {
    public Long countUserByUsername(String username);
    public Long selectUserIdByUsername(String username);
}
