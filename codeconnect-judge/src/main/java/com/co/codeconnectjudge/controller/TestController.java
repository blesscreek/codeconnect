package com.co.codeconnectjudge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('sys:question:add')")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String hello(){
        return "hello";
    }
}
