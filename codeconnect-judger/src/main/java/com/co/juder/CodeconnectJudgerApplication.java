package com.co.juder;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableSwagger2Doc
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class CodeconnectJudgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeconnectJudgerApplication.class, args);
    }

}