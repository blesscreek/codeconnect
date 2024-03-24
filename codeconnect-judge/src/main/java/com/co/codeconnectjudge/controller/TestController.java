package com.co.codeconnectjudge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-20 17:50
 */

@RestController
public class TestController {
    @GetMapping("/test/{id}")
    public String test(@PathVariable Long id){
        System.out.println(id);
        return "后端部署成功";
    }
}