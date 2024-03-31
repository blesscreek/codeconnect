package com.co.codeconnectjudge;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableSwagger2Doc
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class CodeconnectJudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeconnectJudgeApplication.class, args);
    }

}
