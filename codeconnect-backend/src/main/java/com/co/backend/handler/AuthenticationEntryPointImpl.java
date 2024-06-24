package com.co.backend.handler;

import com.alibaba.fastjson.JSON;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-03-31 21:43
 */

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        ResponseResult result = null;
        if ("用户名或密码错误".equals(e.getMessage())) {
            result = new ResponseResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } else {
            result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "用户认证失败请重新登录");
        }
        String json = JSON.toJSONString(result);
        //处理异常
        WebUtils.renderString(response,json);
    }
}
