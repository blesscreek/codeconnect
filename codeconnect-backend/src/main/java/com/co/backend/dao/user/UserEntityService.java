package com.co.backend.dao.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.co.backend.model.po.User;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-04-02 21:44
 */
public interface UserEntityService extends IService<User> {
    public Long countUserByAccount(String account);
    public Long selectUserIdByAccount(String account);
}
