package com.co.judger;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = {"com.co.common","com.co.judger"})
@EnableSwagger2Doc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CodeconnectJudgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeconnectJudgerApplication.class, args);
    }

}