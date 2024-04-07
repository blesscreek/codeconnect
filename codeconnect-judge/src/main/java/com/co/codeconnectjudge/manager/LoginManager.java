package com.co.codeconnectjudge.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.mapper.UserRoleMapper;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.model.po.UserRole;
import com.co.codeconnectjudge.model.vo.RegisterUser;
import com.co.codeconnectjudge.service.UserService;
import com.co.codeconnectjudge.utils.JwtUtil;
import com.co.codeconnectjudge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import static com.co.codeconnectjudge.constant.RedisConstants.LOGIN_CODE_KEY;
import static com.co.codeconnectjudge.constant.UserConstant.USER_TOKEN;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-02 20:29
 */
@Component
public class LoginManager {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;
    private Long roleId = 3L;

    public HashMap<String, String> login(User user) throws StatusFailException {
        if (user.getAccount().length() < 5 || user.getAccount().length() > 10) {
           throw new StatusFailException("账号格式错误");
        }
        if (user.getPassword().length() < 6 || user.getPassword().length() > 15) {
            throw new StatusFailException("密码格式错误");
        }
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
        return map;
    }

    public void logout() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject(LOGIN_CODE_KEY+userid);
    }


    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterUser registerUser) throws StatusFailException {
        if (StringUtils.isEmpty(registerUser.getAccount())) {
            throw new StatusFailException("账号不能为空");
        }
        if (registerUser.getAccount().length() < 5 || registerUser.getAccount().length() > 10) {
            throw new StatusFailException("账号长度应为5~10位");
        }
        if (StringUtils.isEmpty(registerUser.getPassword())) {
            throw new StatusFailException("密码不能为空");
        }
        if (registerUser.getPassword().length() < 6 || registerUser.getPassword().length() > 15) {
            throw new StatusFailException("密码长度为6~15位");
        }
        if (!registerUser.getPassword().equals(registerUser.getCheckPassword())) {
            throw new StatusFailException("两次输入的密码不一致");
        }
        synchronized (registerUser.getAccount().intern()) {
            //账号不能重复
            long count = userService.countUserByAccount(registerUser.getAccount());
            if (count > 0) {
                throw new StatusFailException("账号重复");
            }
            //密码加密
            String encodedPassword = passwordEncoder.encode(registerUser.getPassword());
            //添加数据
            User user = new User();
            user.setAccount(registerUser.getAccount());
            user.setPassword(encodedPassword);
            LocalDateTime currentDateTime = LocalDateTime.now();
            user.setCreateTime(currentDateTime);
            user.setUpdateTime(currentDateTime);
            boolean saveResult = userService.save(user);
            if (!saveResult) {
                throw new StatusFailException("注册失败，请重试");
            }
            Long userId = userService.selectUserIdByAccount(user.getAccount());
            if (userId == null) {
                throw new StatusFailException("注册失败，请重试");
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }
}
