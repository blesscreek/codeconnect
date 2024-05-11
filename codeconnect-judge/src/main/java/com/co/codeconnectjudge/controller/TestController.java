package com.co.codeconnectjudge.controller;

import com.co.codeconnectjudge.dao.user.UserEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-03-27 19:32
 */
@Slf4j
@RestController
public class TestController {
    @Autowired
    private UserEntityService userEntityService;
    @RequestMapping("/hello")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String hello(){
        //获取用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("aa" + authentication.getPrincipal());
        if (authentication.getPrincipal() == "anonymousUser") {
            System.out.println("未登录");
            return "hello";
        }
        System.out.println("已登录");
        return "hello";
    }


}
