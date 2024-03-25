package com.co.codeconnectjudge.service.impl;

import com.co.codeconnectjudge.common.ResponseResult;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.codeconnectjudge.utils.JwtUtil;
import com.co.codeconnectjudge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

import static com.co.codeconnectjudge.constant.RedisConstants.LOGIN_CODE_KEY;
import static com.co.codeconnectjudge.constant.UserConstant.USER_TOKEN;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-03-23
 */
@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager authentication进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        HashMap<String, String> map = new HashMap<>();
        map.put(USER_TOKEN,jwt);
        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject( LOGIN_CODE_KEY + userid, loginUser);
        return new ResponseResult(200,"登录成功",map);
    }
}
